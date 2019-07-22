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
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;

public class pipelineTest2_3 {

	public static void main(String[] args) {
		       // 创建管道工厂
				PipelineOptions options = PipelineOptionsFactory.create();
				// 显式指定PipelineRunner：FlinkRunner必须指定如果不制定则为本地
				options.setRunner(DirectRunner.class); // 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
				// options.setRunner(FlinkRunner.class); //
				// 显式指定PipelineRunner：FlinkRunner（Local模式）//必须指定如果不制定则为本地
				Pipeline pipeline = Pipeline.create(options);// 设置相关管道
				//为了演示显示内存数据集
				 final List<String> LINESa = Arrays.asList(
					     "Aggressive",
					     "Apprehensive");
				 final List<String> LINESb = Arrays.asList(
			  		      "Bold",
					      "Brilliant");
				 PCollection<String> aCollection =pipeline.apply(
							
							Create.of(LINESa)).setCoder(StringUtf8Coder.of());
				 PCollection<String> bCollection =pipeline.apply(
							
							Create.of(LINESb)).setCoder(StringUtf8Coder.of());
				//将两个PCollections与Flatten合并
				 PCollectionList<String> collectionList = PCollectionList.of(aCollection).and(bCollection);
				 PCollection<String> mergedCollectionWithFlatten = collectionList
						    .apply(Flatten.<String>pCollections());
				 
				    System.out.append("合并单词单词有:\r");
				    //设置管道的数据集
					PCollection<String> allCollection = mergedCollectionWithFlatten.apply("aTrans", ParDo.of(new DoFn<String, String>(){
						  @ProcessElement
						  public void processElement(ProcessContext c) {
							 
						   
						      c.output(c.element());
						      System.out.append(""+c.element()+"\r");
						  
						  }
						}));
		
				
				
					pipeline.run().waitUntilFinish();		
					
	}

}
