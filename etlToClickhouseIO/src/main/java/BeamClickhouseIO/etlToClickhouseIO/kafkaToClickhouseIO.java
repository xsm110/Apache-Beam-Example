package BeamClickhouseIO.etlToClickhouseIO;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.Schema.FieldType;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.grpc.v1p13p1.com.google.gson.Gson;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.joda.time.Duration;
import avro.shaded.com.google.common.collect.ImmutableMap;
//引入clickhouse
import org.apache.beam.sdk.io.clickhouse.ClickHouseIO;
import org.apache.beam.sdk.io.elasticsearch.ElasticsearchIO;

public class kafkaToClickhouseIO {

	public static void main(String[] args) {

		// 创建管道工厂
		PipelineOptions options = PipelineOptionsFactory.create();
		// 显式指定PipelineRunner：FlinkRunner必须指定如果不制定则为本地
		options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
		// options.setRunner(FlinkRunner.class); //
		// 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
		Pipeline pipeline = Pipeline.create(options);// 设置相关管道
		PCollection<KafkaRecord<String, String>> lines = // 这里kV后说明kafka中的key和value均为String类型
				pipeline.apply(KafkaIO.<String, String>read().withBootstrapServers("101.201.56.77:9092")// 必需设置kafka的服务器地址和端口
						.withTopic("TopicAlarm")// 必需，设置要读取的kafka的topic名称
						
						.withKeyDeserializer(StringDeserializer.class)// 必需序列化key
						.withValueDeserializer(StringDeserializer.class)// 必需序列化value
						.updateConsumerProperties(ImmutableMap.<String, Object>of("auto.offset.reset", "earliest")));// 这个属性kafka最常见的
		// 设置Schema 的的字段名称和类型
		final Schema type = Schema.of(Schema.Field.of("alarmid", FieldType.STRING), Schema.Field.of("alarmTitle", FieldType.STRING),
				Schema.Field.of("deviceModel", FieldType.STRING), Schema.Field.of("alarmSource", FieldType.INT32), Schema.Field.of("alarmMsg", FieldType.STRING));
		// 从kafka中读出的数据转换成AlarmTable实体对象
		PCollection<AlarmTable> kafkadata = lines.apply("Remove Kafka Metadata", ParDo.of(new DoFn<KafkaRecord<String, String>,AlarmTable>() {
			private static final long serialVersionUID = 1L;

			@ProcessElement
			public void processElement(ProcessContext ctx) {
				System.out.print("kafka序列化之前数据----：" + ctx.element().getKV().getValue() + "\r\n");
				Gson gon = new Gson();
				AlarmTable modelTable = null;
				try {// 进行序列号代码
					modelTable = gon.fromJson(ctx.element().getKV().getValue(), AlarmTable.class);
					System.out.print("kafka序列号：" + modelTable.getAlarmMsg());
				} catch (Exception e) {
					System.out.print("json序列化出现问题：" + e);
				}
				ctx.output(modelTable);// 回传实体
			}
		}));
		String[] addresses = { "http://101.201.56.77:9200/" };
		PCollection<String> jsonCollection=kafkadata
				.setCoder(AvroCoder.of(AlarmTable.class))
				.apply("covert json", ParDo.of(new DoFn<AlarmTable, String>() {
			private static final long serialVersionUID = 1L;

			@ProcessElement
			public void processElement(ProcessContext ctx) {
				
				Gson gon = new Gson();
				String	jString="";
				try {// 进行序列号代码
				jString = gon.toJson(ctx.element());
					System.out.print("序列化后的数据：" + jString);
				} catch (Exception e) {
					System.out.print("json序列化出现问题：" + e);
				}
				ctx.output(jString);// 回传实体
			}
		}));
		
		// 所有的Beam 数据写入ES的数据统一转换成json 才可以正常插入
		jsonCollection.apply(
				ElasticsearchIO.write().withConnectionConfiguration(ElasticsearchIO.ConnectionConfiguration.create(addresses, "alarm", "TopicAlarm")

		));

		PCollection<Row> modelPCollection = kafkadata
			//	 .setCoder(AvroCoder.of(AlarmTable.class))//前面设置了后面不需要重复设置编码
				.apply(ParDo.of(new DoFn<AlarmTable, Row>() {// 实体转换成Row
					private static final long serialVersionUID = 1L;

					@ProcessElement
					public void processElement(ProcessContext c) {
						AlarmTable modelTable = c.element();
						System.out.print(modelTable.getAlarmMsg());
						Row alarmRow = Row.withSchema(type)
								.addValues(modelTable.getAlarmid(), modelTable.getAlarmTitle(), modelTable.getDeviceModel(), modelTable.getAlarmSource(), modelTable.getAlarmMsg())
								.build();// 实体赋值Row类型
						System.out.print("到------行了" + alarmRow.toString());
						c.output(alarmRow);
					}
				}));
		// 写入 es

		// 写入ClickHouse
		modelPCollection.setRowSchema(type).apply(ClickHouseIO.<Row>write("jdbc:clickhouse://101.201.56.77:8123/Alarm", "AlarmTable").withMaxRetries(3)// 重试次数
				.withMaxInsertBlockSize(5) // 添加最大块的大小
				.withInitialBackoff(Duration.standardSeconds(5))// 初始退回时间
				.withInsertDeduplicate(true) // 重复数据是否删除
				.withInsertDistributedSync(false));
		pipeline.run().waitUntilFinish();
	}

}
