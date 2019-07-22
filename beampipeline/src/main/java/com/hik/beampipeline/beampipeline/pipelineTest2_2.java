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
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

public class pipelineTest2_2 {

	public static void main(String[] args) {
		       // 创建管道工厂
				PipelineOptions options = PipelineOptionsFactory.create();
				// 显式指定PipelineRunner：FlinkRunner必须指定如果不制定则为本地
				options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
				// options.setRunner(FlinkRunner.class); //
				// 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
				Pipeline pipeline = Pipeline.create(options);// 设置相关管道
				//为了演示显示内存数据集
				 final List<String> LINES = Arrays.asList(
					     "Aggressive",//有进取心的
					      "Bold",//大胆的,勇敢的
					     "Apprehensive",//有理解能力的
					      "Brilliant");//才华横溢的
				
				//设置管道的数据集
				PCollection<String> dbRowCollection =pipeline.apply(
						
						Create.of(LINES)).setCoder(StringUtf8Coder.of());
				final TupleTag<String> startsWithATag = new TupleTag<String>(){};
				final TupleTag<String> startsWithBTag = new TupleTag<String>(){};
				PCollectionTuple mixedCollection =
				    dbRowCollection.apply(ParDo
				        .of(new DoFn<String, String>() {
				          @ProcessElement
				          public void processElement(ProcessContext c) {
				            if (c.element().startsWith("A")) {
				              // 返回首字母带有"A"的数据集。
				              c.output(c.element());
				         	  System.out.append("A开头的单词有:"+c.element()+"\r");
				            } else if(c.element().startsWith("B")) {
				              // // 返回首字母带有"B"的数据集。
				              c.output(startsWithBTag, c.element());
				              System.out.append("B开头的单词有:"+c.element()+"\r");
				            }
				          }
				        })
				        // Specify main output. In this example, it is the output
				        // with tag startsWithATag.
				        .withOutputTags(startsWithATag,
				        // Specify the output with tag startsWithBTag, as a TupleTagList.
				                        TupleTagList.of(startsWithBTag)));
				
				
				
					pipeline.run().waitUntilFinish();		
					
	}

}
