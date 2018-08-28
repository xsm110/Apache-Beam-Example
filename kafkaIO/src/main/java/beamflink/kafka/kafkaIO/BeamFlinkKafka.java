package beamflink.kafka.kafkaIO;
import org.apache.beam.runners.flink.FlinkRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.joda.time.Duration;
import avro.shaded.com.google.common.collect.ImmutableMap;
public class BeamFlinkKafka {

	public static void main(String[] args) {
		PipelineOptions options = PipelineOptionsFactory.create(); //创建管道工厂
		// FlinkPipelineOptions options = PipelineOptionsFactory.as(FlinkPipelineOptions.class);
		// options.setStreaming(true);
		// options.setAppName("app_test");
		// options.setJobName("flinkjob");
		// options.setFlinkMaster("localhost:6123");
		// options.setParallelism(10);//设置flink 的并行度
		// options.setExternalizedCheckpointsEnabled(true);
		// options.setCheckpointingInterval();
		// options.setCheckpointTimeoutMillis(5000);
		// List<String> list=new ArrayList<>();
		// list.add("D://kafkaIOBeam.jar");
		// options.setFilesToStage(list);
		// options.setTempLocation("D:\\kafkaIOBeam.jar");

		
		
		options.setRunner(FlinkRunner.class); // 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
		Pipeline pipeline = Pipeline.create(options);//设置相关管道
		PCollection<KafkaRecord<String, String>> lines = //这里kV后说明kafka中的key和value均为String类型
				pipeline.apply(KafkaIO.<String, String>read().withBootstrapServers("10.192.32.202:11092,10.192.32.202:12092,10.192.32.202:13092")// 必需设置kafka的服务器地址和端口
						.withTopic("testmsg")// 必需，设置要读取的kafka的topic名称
						.withKeyDeserializer(StringDeserializer.class)// 必需序列化key
						.withValueDeserializer(StringDeserializer.class)// 必需序列化value
						.updateConsumerProperties(ImmutableMap.<String, Object>of("auto.offset.reset", "earliest")));//这个属性kafka最常见的
		// 为输出的消息类型。或者进行处理后返回的消息类型
	 	PCollection<String> kafkadata = lines.apply("Remove Kafka Metadata", ParDo.of(new DoFn<KafkaRecord<String, String>, String>() { 
			private static final long serialVersionUID = 1L;
			@ProcessElement
			public void processElement(ProcessContext ctx) {
				System.out.print("输出的分区为----：" + ctx.element().getKV());
				ctx.output(ctx.element().getKV().getValue());// 其实我们这里是把"张海涛在发送消息***"进行返回操作
			}
		}));
		PCollection<String> windowedEvents = kafkadata.apply(Window.<String>into(FixedWindows.of(Duration.standardSeconds(5))));
		PCollection<KV<String, Long>> wordcount = windowedEvents.apply(Count.<String>perElement()); // 统计每一个kafka消息的Count
		PCollection<String> wordtj = wordcount.apply("ConcatResultKVs", MapElements.via( // 拼接最后的格式化输出（Key为Word，Value为Count）
				new SimpleFunction<KV<String, Long>, String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String apply(KV<String, Long> input) {
						System.out.print("进行统计：" + input.getKey() + ": " + input.getValue());
						return input.getKey() + ": " + input.getValue();
					}
				}));
		wordtj.apply(KafkaIO.<Void, String>write()
				.withBootstrapServers("10.192.32.202:11092,10.192.32.202:12092,10.192.32.202:13092")//设置写会kafka的集群配置地址
				.withTopic("senkafkamsg")//设置返回kafka的消息主题
				// .withKeySerializer(StringSerializer.class)//这里不用设置了，因为上面 Void 
				.withValueSerializer(StringSerializer.class)
				// Dataflow runner and Spark 兼容， Flink 对kafka0.11才支持。我的版本是0.10不兼容
				//.withEOS(20, "eos-sink-group-id")
				.values() // 只需要在此写入默认的key就行了，默认为null值
		); // 输出结果
		pipeline.run().waitUntilFinish();

		
		
		
		
		
		
		// PCollection<KafkaRecord<byte[],byte[]>> kafkadata =
		// pipeline.apply(KafkaIO.<byte[], byte[]>read()//<byte[], byte[]>
		// 设置为了读取二进制私有协议的 数据
		// .withBootstrapServers("10.192.32.202:11092,10.192.32.202:12092,10.192.32.202:13092")//
		// 设置kafka集群地址
		// .withTopics(ImmutableList.of("topic_a", "topic_b"))//
		// 设置kafka消费主题，单个设置为 // .withTopic("my_topic")
		// // 配置键值对编码，这里也设置成了 二进制
		// .withKeyDeserializer(ByteArrayDeserializer.class)
		// .withValueDeserializer(ByteArrayDeserializer.class)
		//
		// // 配置缓冲字节大小
		// .updateConsumerProperties(ImmutableMap.of("receive.buffer.bytes",
		// 1024 * 1024))
		//
		// //设置我们平时用kafka的那些设置
		// .updateConsumerProperties(ImmutableMap.of("group_id","my_beam_app_1","auto.offset.reset","earliest",ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG,10,
		// ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 5,
		// ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 8))
		// .withMaxNumRecords(50)//设置最大读取条数
		// // 自定义函数计算记录时间戳(缺省为处理时间)
		// // .withTimestampFn(new MyTypestampFunction())
		// // 自定义 watermark函数 (默认是 timestamp)
		// // .withWatermarkFn(new MyWatermarkFunction())
		// .withLogAppendTime()
		// // 最后,如果你不需要Kafka 元数据的话,你可以丢弃它
		// //.withoutMetadata()
		// // restrict reader to committed messages on Kafka (see method
		// // documentation).
		// // 限制读取时候去提交数据便宜
		// .withReadCommitted()
		//
		// // offset consumed by the pipeline can be committed back.
		// // 通过管道返回的offset 进行提交偏移
		// .commitOffsetsInFinalize()
		// // 设置10秒中超时
		// .withMaxNumRecords(10)
		//
		// );
		// 读取本地文件，构建第一个PTransform
		// .apply("byteToString", ParDo.of(new
		// DoFn<byte[],KafkaRecord<byte[],byte[]>>() { // 对文件中每一行进行处理（实际上Split）
		//
		// @ProcessElement
		// public void processElement(ProcessContext c) {
		// for (KafkaRecord<byte[],byte[]> word : c.element()) {
		// if (word!=null) {
		// c.output(word.getKV());
		//
		// }
		// }
		// }
		//
		// }))

	}

}
