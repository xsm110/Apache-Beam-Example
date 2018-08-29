package com.BeamTextIO.TextIO;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.apache.beam.sdk.transforms.DoFn.ProcessElement;
import org.apache.beam.sdk.values.KV;

public class DEMO {

	public static void main(String[] args) {
		PipelineOptions pipelineOptions = PipelineOptionsFactory.create();//创建管道工厂
		pipelineOptions.setRunner(DirectRunner.class);//设置运行的模型，现在一共有3种

		
		// DataflowPipelineOptions dataflowOptions =
		// pipelineOptions.as(DataflowPipelineOptions.class);
		// dataflowOptions.setRunner(DataflowRunner.class);
		Pipeline pipeline = Pipeline.create(pipelineOptions);//设置相应的管道
		//根据文件路径读取文件内容
		pipeline.apply(TextIO.read().from("/usr/local/text.txt")).apply("ExtractWords", ParDo.of(new DoFn<String, String>() {
			@ProcessElement
			public void processElement(ProcessContext c) {
				for (String word : c.element().split(" ")) {//根据空格进行读取数据，里面可以用Luma 表达式写
					if (!word.isEmpty()) {
						c.output(word);
						System.out.println("读文件中的数据："+word);
					}
				}
			}
		})).apply(Count.<String>perElement())
		.apply("FormatResult", MapElements.via(new SimpleFunction<KV<String, Long>, String>() {
			@Override
			public String apply(KV<String, Long> input) {
				return input.getKey() + ": " + input.getValue();
			}
		})).apply(TextIO.write().to("/usr/local/textcount"));//进行输出到文件夹下面
		pipeline.run().waitUntilFinish();
		
		
		
	

	}

}
