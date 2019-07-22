package com.hik.beampipeline.beampipeline;

import java.util.Arrays;
import java.util.List;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.io.kafka.KafkaRecord;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.schemas.Schema.FieldType;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.vendor.grpc.v1p13p1.com.google.gson.Gson;
import org.apache.kafka.common.serialization.StringDeserializer;
import avro.shaded.com.google.common.collect.ImmutableMap;

public class pipelineTest2_4 {

	public static void main(String[] args) {
		// 创建管道工厂
		PipelineOptions options = PipelineOptionsFactory.create();
		// 显式指定PipelineRunner：FlinkRunner必须指定如果不制定则为本地
		options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
		// options.setRunner(FlinkRunner.class); //
		// 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
		Pipeline pipeline = Pipeline.create(options);// 设置相关管道
		// 读取mysql 数据集

		// 读取kafka数据集
		PCollection<KafkaRecord<String, String>> lines = // 这里kV后说明kafka中的key和value均为String类型
				pipeline.apply(KafkaIO.<String, String>read().withBootstrapServers("101.201.56.77:9092")// 必需设置kafka的服务器地址和端口
						.withTopic("TopicAlarm")// 必需，设置要读取的kafka的topic名称

						.withKeyDeserializer(StringDeserializer.class)// 必需序列化key
						.withValueDeserializer(StringDeserializer.class)// 必需序列化value
						.updateConsumerProperties(ImmutableMap.<String, Object>of("auto.offset.reset", "earliest")));// 这个属性kafka最常见的
		// 设置Schema 的的字段名称和类型
		final Schema type = Schema.of(Schema.Field.of("alarmid", FieldType.STRING), Schema.Field.of("alarmTitle", FieldType.STRING),
				Schema.Field.of("deviceModel", FieldType.STRING), Schema.Field.of("alarmSource", FieldType.INT32), Schema.Field.of("alarmMsg", FieldType.STRING));
		// 从kafka中读出的数据
		PCollection<KV<String, String>> kafkadata = lines.apply("Remove Kafka Metadata", ParDo.of(new DoFn<KafkaRecord<String, String>, KV<String, String>>() {
			private static final long serialVersionUID = 1L;

			@ProcessElement
			public void processElement(ProcessContext ctx) {
				System.out.print("kafka序列化之前数据----：" + ctx.element().getKV().getValue() + "\r\n");
				Gson gon = new Gson();

				ctx.output(ctx.element().getKV());// 回传实体
			}
		}));
		// 从

		final List<String> LINESa = Arrays.asList("Aggressive", "Apprehensive");
		final List<String> LINESb = Arrays.asList("Bold", "Brilliant");
		PCollection<String> aCollection = pipeline.apply(

				Create.of(LINESa)).setCoder(StringUtf8Coder.of());
		PCollection<String> bCollection = pipeline.apply(

				Create.of(LINESb)).setCoder(StringUtf8Coder.of());
		// 将两个PCollections与Flatten合并
		PCollectionList<String> collectionList = PCollectionList.of(aCollection).and(bCollection);
		PCollection<String> mergedCollectionWithFlatten = collectionList.apply(Flatten.<String>pCollections());

		System.out.append("合并单词单词有:\r");
		// 设置管道的数据集
		PCollection<String> allCollection = mergedCollectionWithFlatten.apply("aTrans", ParDo.of(new DoFn<String, String>() {
			@ProcessElement
			public void processElement(ProcessContext c) {

				c.output(c.element());
				System.out.append("" + c.element() + "\r");

			}
		}));

		pipeline.run().waitUntilFinish();

	}

}
