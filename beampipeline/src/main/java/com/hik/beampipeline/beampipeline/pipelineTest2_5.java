package com.hik.beampipeline.beampipeline;

import java.util.Arrays;
import java.util.List;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.transforms.Flatten;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

import avro.shaded.com.google.common.collect.Sets;

public class pipelineTest2_5 {

	public static void main(String[] args) {
		// 创建管道工厂
		PipelineOptions options = PipelineOptionsFactory.create();
		// 显式指定PipelineRunner：FlinkRunner必须指定如果不制定则为本地
		options.setRunner(DirectRunner.class); // 生产环境关闭
		// options.setRunner(FlinkRunner.class); //生成环境打开

		Pipeline pipeline = Pipeline.create(options);// 设置相关管道
		// 为了演示显示内存数据集
		// 叫号数据
		final List<KV<String, String>> txtnoticelist = Arrays.asList(KV.of("DS-2CD2326FWDA3-I", "101号顾客请到3号柜台"), KV.of("DS-2CD2T26FDWDA3-IS", "102号顾客请到1号柜台"),
				KV.of("DS-2CD6984F-IHS", "103号顾客请到4号柜台"), KV.of("DS-2CD7627HWD-LZS", "104号顾客请到2号柜台"));
		//AI行为分析消息
		final List<KV<String, String>> aimessagelist = Arrays.asList(KV.of("DS-2CD2326FWDA3-I", "CMOS智能半球网络摄像机,山东省济南市解放路支行3号柜台,type=2,display_image=no"),
				KV.of("DS-2CD2T26FDWDA3-IS", "CMOS智能筒型网络摄像机,山东省济南市甸柳庄支行1号柜台,type=2,display_image=no"),
				KV.of("DS-2CD6984F-IHS", "星光级全景拼接网络摄像机,山东省济南市市中区支行4号柜台,type=2,display_image=no"), KV.of("DS-2CD7627HWD-LZS", "全结构化摄像机,山东省济南市市中区支行2号柜台,type=2,display_image=no"));
		PCollection<KV<String, String>> notice = pipeline.apply("CreateEmails", Create.of(txtnoticelist));
		PCollection<KV<String, String>> message = pipeline.apply("CreatePhones", Create.of(aimessagelist));
		final TupleTag<String> noticeTag = new TupleTag<>();
		final TupleTag<String> messageTag = new TupleTag<>();
		PCollection<KV<String, CoGbkResult>> results = KeyedPCollectionTuple.of(noticeTag, notice).and(messageTag, message).apply(CoGroupByKey.create());
		System.out.append("合并分组后的结果:\r");
		PCollection<String> contactLines = results.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, String>() {
			private static final long serialVersionUID = 1L;
			@ProcessElement
			public void processElement(ProcessContext c) {
				KV<String, CoGbkResult> e = c.element();
				String name = e.getKey();
				Iterable<String> emailsIter = e.getValue().getAll(noticeTag);
				Iterable<String> phonesIter = e.getValue().getAll(messageTag);
				System.out.append("" + name + ";" + emailsIter + ";" + phonesIter + ";" + "\r");
			}
		}));
		pipeline.run().waitUntilFinish();

	}

}
