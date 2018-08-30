package com.Beam.Hadoop.Hdfs;

import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.hdfs.HadoopFileSystemOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import org.apache.hadoop.conf.Configuration;

import avro.shaded.com.google.common.collect.ImmutableList;

public class BeamHdfsIO {

	public static void main(String[] args) {
	
			Configuration conf = new Configuration();// 配置hadoop 的配置文件
			conf.set("fs.default.name", "hdfs://192.168.220.140:9000"); // hadoop hdfs
//			conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

			HadoopFileSystemOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(HadoopFileSystemOptions.class);

			options.setHdfsConfiguration(ImmutableList.of(conf));
			options.setRunner(DirectRunner.class);

			Pipeline pipeline = Pipeline.create(options);

			// 根据文件路径读取文件内容
			pipeline.apply(TextIO.read().from("hdfs://192.168.220.140:9000/user/lenovo/testfile/test.txt")).apply("ExtractWords", ParDo.of(new DoFn<String, String>() {
				
				@ProcessElement
				public void processElement(ProcessContext c) {

					for (String word : c.element().split(" ")) {// 根据空格进行读取数据，里面可以用Luma 表达式写
					
						if (!word.isEmpty()) {
							c.output(word);
							System.out.println( word+"\n");
						}
					}
				}
			}));
			pipeline.run().waitUntilFinish();

			System.exit(0);
			}

	}


