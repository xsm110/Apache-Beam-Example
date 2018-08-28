# Apache-Beam-Example
=======================
Apache Beam Example 中国开源社区

里面是Beam 项目实战的例子，欢迎大家进入进行更新。想加入进来的请联系微信cyrjkj

以下文章转自我发的
系列文章第一篇回顾[Apache Beam实战指南之基础入门](https://mp.weixin.qq.com/s?__biz=MzU1NDA4NjU2MA==&mid=2247486109&amp;idx=1&amp;sn=07505c089eda36bfa474827ad577ae61&source=41#wechat_redirect)



Beam
-----
Apache Beam是用于定义批处理和流数据并行处理流水线的统一模型，以及一组用于构建管道的特定于语言的SDK，用于在分布式处理后端执行它们的Runners，包括Apache Apex，Apache Flink，Apache Spark和Google Cloud Dataflow。官方网站为 https://beam.apache.org

#一．概述
--------
大数据处理领域的一大问题是：开发者经常要用到很多不同的技术、框架、API、开发语言和 SDK。取决于需要完成的是什么任务，以及在什么情况下进行，开发者很可能会用 MapReduce 进行批处理，用 Apache Spark SQL 进行交互请求（ interactive queries），用 Apache Flink 实时流处理，还有可能用到基于云端的机器学习框架。

近两年开启的开源大潮，为大数据开发者提供了十分富余的工具。但这同时也增加了开发者选择合适的工具的难度，尤其对于新入行的开发者来说。这很可能拖慢、甚至阻碍开源工具的发展：把各种开源框架、工具、库、平台人工整合到一起所需工作之复杂，是大数据开发者常有的抱怨之一，也是他们支持专有大数据平台的首要原因。

#二．Beam 发展历史
--------
Beam在2016年2月成为Apache孵化器项目，并在2016年12月升级成为Apache基金会的顶级项目。通过十五个月的努力，一个稍显混乱的代码库，从多个组织合并，已发展成为数据处理的通用引擎，集成多个处理数据框架，可以做到跨环境。

Beam经过三个孵化器版本和三个后孵化器版本的演化和改进，最终在2017年5月17日迎来了它的第一个稳定版2.0.0。在发布稳定版本3个月以来，Apache Beam 已经出现明显的增长，无论是通过官方还是社区的贡献数量。Apache Beam 在谷歌云方面已经展示出了“才干”。

Beam 2.0.0改进了用户体验，重点在框架跨环境中的无缝移植能力，这些执行环境包括执行引擎、操作系统、本地集群、云端，以及数据存储系统。Beam的其他特性还包括如下几点：
API稳定性和对未来版本的兼容性。
有状态的数据处理模式，高效的支持依赖于数据的计算。
支持用户扩展的文件系统，支持Hadoop分布式发文件系统及其他。
提供了一个度量指标系统，可用于跟踪管道的执行状况。
网上已经有很多人写了2.0.0版本之前的相关资料，但是2.0.0版本后API很多写法变动较大，本文将带着大家从零基础到Apache Beam入门。

#三．Beam 应用场景
-----------------
Google Cloud、PayPal、Talend 等公司都在使用 Beam，国内有阿里巴巴，还有一些其他单位，如百度、金山、苏宁、九次方大数据、360、慧聚数通信息技术有限公司等，另外还有一些大数据公司的架构师或研发人员也一起进行研究。Apache Beam 中文社区正在集成一些工作中的runners 和sdk IO，包括人工智能、机器学习和时序数据库等一些功能。

应用场景举几个例子：
1.      Beam可以用于ETL Job 任务
         Beam的数据可以通过SDKs的IO接入，通过管道可以用后面的Runners做清洗。
2.      Beam数据仓库快速切换、跨仓库
         由于Beam 的数据源是多样的IO，所以用Beam可以快速切换任何数据仓库。
3.      Beam计算处理平台切换、跨平台
         Runners目前提供了3-4种可以切换和跨越的平台，随着Beam的强大应该会有更多的平台提供给大家使用。
 
#四．Beam 运行流程                                                                 
----------------
![数据处理流程](https://images-cdn.shimo.im/dG1QZ7WAq5EafnLB/3.png!thumbnail "数据处理流程")
   
4-1 数据处理流程
                 
如图4-1 所示，Apache Beam大体运行流程分成三大部分：
##1.Modes

       Modes是Beam的模型或叫数据来源的IO，它是由多种数据源或仓库的IO组成，数据源支持两种，一种是批处理，一种是流处理。具体我们看一下第五小节  Beam Model
       
##2.Pipeline

      Pipeline是Beam 的管道，所有的批处理或流处理都要通过这个管道把数据传输到后端的计算平台。这个管道现在是唯一的。数据源可以切换多种，计算平台或处理平台也支持多种。需要注意的是唯独管道只有一条，它的作用是连接数据和Runtimes平台。
      
##3.Runtimes

     Runtimes是大数据计算或处理平台，现在支持Apache Flink、Apache Spark、Direct Pipeline和Google Clound Dataflow这四种。其中 Apache Flink和Apache Spark同时支持本地和云端。Direct Pipeline仅支持本地，Google Clound Dataflow 仅支持云端。除此之外，后期Beam 国外研发团队还会集成其他的大数据计算平台。由于谷歌未进入中国，目前国内开发人员在工作中对谷歌云的使用应该不是很多，还是以前两种为主。为了使读者读完文章后能快速学习且更贴近实际工作环境，后续文章中我会以前两种作为大数据计算或处理平台进行演示。

#五．Beam Model及其工作流程
-----------

*Beam Model指的是Beam的编程范式，即Beam SDK背后的设计思想。在介绍Beam Model之前，先简要介绍一下Beam Model要处理的问题域与一些基本概念。

*数据源类型。分布式数据来源类型一般可以分为两类，有界的数据集和无界的数据流。有界的数据集，比如一个Ceph中的文件，一个Mongodb表等，特点是数据已经存在，表示一个数据集的一个已知的，固定的大小，一般在磁盘存在，不会突然消失。而无界的数据流，比如kafka中流过来的数据流，这种数据的特点是，数据动态流入，没有边界，无法全部持久化到磁盘上。所以Beam 框架设计的时候就设计了处理一种有界的数据，批处理。另一种就是无界的数据，也就是流处理。
时间。在分布式框架的时间处理有两种，一种是全量计算的，另一种是部分增量计算的。我给大家举个例子:例如我们玩“王者农药”游戏，游戏的数据需要实时的流向服务器，计算掉血情况，在不断变化时间去计算，但是，排行榜的数据则是在全部玩家一定的时间内的名词排名，例如一周，一个月。Beam 从中就设计了这两种情况的处理。
乱序。对于流处理数据的方式讲，数据到达大体分两种，一种是 按照Process Time定义时间窗口 ，这种是不用考虑乱序问题，因为都是关闭这个窗口才进行下一个窗口的操作，有一种等待的性质，所以执行顺序都是保持顺序的。而Event Time定义的时间窗口则不需要等待，可能这块的操作没有处理完，就直接执行了下一个操作，造成了一种消息顺序处理结果不是按顺序排序了，例如我们订单的消息，分布式的处理，如果一台服务器处理速度比较慢，刚好是下单的操作。而用户支付的服务器速度非常快的情况下，这个最后的订单操作时间轴就会出现一种情况，下单在支付的后面。 对于这种情况，如何确定迟到数据，以及对于迟到数据如何处理通常是很麻烦的事情。
Beam Model处理的目标数据是无界的时间乱序数据流，不考虑时间顺序或是有界的数据集可看做是无界乱序数据流的一个特例。Beam Model从下面四个维度归纳了用户在进行数据处理的时候需要考虑的问题：

What。如何对数据进行计算?例如，机器学习中对数据集进行训练学习模型。在Beam SDK中由Pipeline中的操作符指定。

Where。数据在什么范围中计算?例如，基于Process-Time的时间窗口，基于Event-Time的时间窗口，滑动窗口等等。在BeamSDK中由Pipeline中的窗口指定。

When。何时将计算结果输出?例如，在1小时的Event-Time时间窗口中，每隔1分钟，将当前窗口计算结果输出。在Beam SDK中由Pipeline中的Watermark和触发器指定。

How。迟到数据如何处理?例如，将迟到数据计算增量结果输出，或是将迟到数据计算结果和窗口内数据计算结果合并成全量结果输出。在Beam SDK中由Accumulation指定。

Beam Model将”WWWH“四个维度抽象出来组成了Beam SDK，用户在基于Beam SDK构建数据处理业务逻辑时，在每一步只需要根据业务需求按照这四个维度调用具体的API
即可生成分布式数据处理Pipeline，并提交到具体执行引擎上执行。“WWWH”四个维度只是在业务的角度去看待问题，并不是全部适用于自己的业务。做技术架构一定结合自己的业务去使用相应的技术特性或框架。Beam 做为“一统”的框架，为开发者带来了方便。


#六．SDKs
---------
Beam SDK给上层应用的开发者提供了一个统一的编程接口，开发者不需要了解底层的具体的大数据平台的开发接口是什么，直接通过Beam SDK的接口就可以开发数据处理的加工流程，不管输入是用于批处理的有界数据集，还是流式的无界数据集。对于有界或无界的输入数据，Beam SDK都使用相同的类来表现，并且使用相同的转换操作进行处理。Beam SDK拥有不同编程语言的实现，目前已经完整地提供了Java，Python的SDK还在开发中，相信未来会发布更多不同编程语言的SDK。

Beam 2.0的SDKs 现在有：

*Amqp
高级消息队列协议。

*Cassandra
Cassandra 是一个 NoSQL 列族 (column family) 实现，使用由 Amazon Dynamo 引入的架构方面的特性来支持 Big Table 数据模型。Cassandra 的一些优势如下所示：
　　高度可扩展性和高度可用性，没有单点故障
　　NoSQL 列族实现
　　非常高的写入吞吐量和良好的读取吞吐量
　　类似 SQL 的查询语言（从 0.8版本 起），并通过二级索引支持搜索
　　可调节的一致性和对复制的支持灵活的模式
*Elasticesarch
一个实时的分布式搜索引擎。

*Google-cloud-platform
谷歌云IO

*Hadoop-file-system
操作hadoop 的文件系统的IO

*Hadoop-hbase
操作hadoop 上的hbase 的接口IO

*Hcatalog
Hcatalog是apache开源的对于表和底层数据管理统一服务平台。

*Jdbc
连接各种数据库的数据库连接器

*Jms
Java 消息服务（Java Message Service，简称JMS）是用于访问企业消息系统的开发商中立的API。企业消息系统可以协助应用软件通过网络进行消息交互。JMS 在其中扮演的角色与JDBC 很相似，正如JDBC 提供了一套用于访问各种不同关系数据库的公共API，JMS 也提供了独立于特定厂商的企业消息系统访问方式。

*Kafka
处理流数据的轻量级大数据消息系统，或叫消息总线。

*Kinesis
对接亚马逊的服务，可以构建用于处理或分析流数据的自定义应用程序，以满足特定需求。

*Mongodb
MongoDB 是一个基于分布式文件存储的数据库。

*Mqtt
是IBM开发的一个即时通讯协议。

*Solr
亚实时的分布式搜索引擎技术。

*xml
一种数据格式。

#七．Pipeline Runners
-----------------
Beam Pipeline Runner将用户用Beam模型定义开发的处理流程翻译成底层的分布式数据处理平台支持的运行时环境。在运行Beam程序时，需要指明底层的正确Runner类型，针对不同的大数据平台，会有不同的Runner。目前Flink、Spark、Apex以及谷歌的Cloud DataFlow都有支持Beam的Runner。

需要注意的是，虽然Apache Beam社区非常希望所有的Beam执行引擎都能够支持Beam SDK定义的功能全集，但是在实际实现中可能无法达到这一期望。例如，基于MapReduce的Runner显然很难实现和流处理相关的功能特性。就目前状态而言，对Beam模型支持最好的就是运行于谷歌云平台之上的Cloud Dataflow，以及可以用于自建或部署在非谷歌云之上的Apache Flink。当然，其它的Runner也正在迎头赶上，整个行业也在朝着支持Beam模型的方向发展。

Beam 2.0 的Runners 框架：
======================
*Apex
诞生于2015年6月的Apache Apex，其同样源自DataTorrent及其令人印象深刻的RTS平台，其中包含一套核心处理引擎、仪表板、诊断与监控工具套件外加专门面向数据科学家用户的图形流编程系统dtAssemble。主要用于流处理，常用于物联网等场景。

*Direct-java
本地处理和运行runner。

*Flink_2.10
Flink 是一个针对流数据和批数据的分布式处理引擎。

*Gearpump
Gearpump是一个基于Akka Actor的轻量级的实时流计算引擎。今天的流平台需要能处理来自各种移动端和物联网设备的海量数据，系统要能不间断地提供服务，对数据的处理要能做到不丢失不重复，对各种软硬件错误能平滑处理，对用户的输入要能实时响应。除了这些系统层面的需求外，用户层面的接口还要能做到丰富而灵活，一方面，平台要提供足够丰富的基础设施，能最简化应用程序的编写；另一方面，这个平台应提供具有表现力的编程API，让用户能灵活表达各种计算，并且整个系统可以定制，允许用户选择调度策略和部署环境，允许用户在不同的指标间做折中取舍，以满足特定的需求。Akka Actor提供了通信、并发、隔离、容错的基础设施， Gearpump通过把抽象层次提升到Actor这一层，屏蔽了底层的细节，专注于流处理需求本身，能更简单而又高效地解决上述问题。

*Dataflow
2016 年 2 月份，谷歌及其合作伙伴向 Apache 捐赠了一大批代码，创立了孵化中的 Beam 项目（最初叫 Apache Dataflow）。这些代码中的大部分来自于谷歌 Cloud Dataflow SDK——开发者用来写流处理和批处理管道（pipelines）的库，可在任何支持的执行引擎上运行。当时，支持的主要引擎是谷歌 Cloud Dataflow。

*Spark
Apache Spark是一个正在快速成长的开源集群计算系统。Apache Spark生态系统中的包和框架日益丰富，使得Spark能够执行高级数据分析。Apache Spark的快速成功得益于它的强大功能和易用性。相比于传统的MapReduce大数据分析，Spark效率更高、运行时速度更快。Apache Spark 提供了内存中的分布式计算能力，具有Java、 Scala、Python、R四种编程语言的API编程接口。

#八．开发第一个程序
------------------

*8.1开发环境
1.下载安装 JDK 7 或更新的版本，检测 JAVA_HOME环境变量。本文示例使用的是JDK1.8。

2.下载maven并配置，本文示例使用的是maven-3.3.3。

3.开发环境 myeclipse、Spring Tool Suite 、IntelliJ IDEA，这个可以按照个人喜好，本文示例用的是STS。
*8.2 开发第一个wordCount程序并且运行
通过通读全篇文章并实际动手操作后，大家将能从听说过Beam的懵懂状态达到入门阶段。下面大家就跟着我一步一步编写第一个wordCount 程序吧。

1、新建一个maven 项目
　　　
![beam](https://images-cdn.shimo.im/4ybgX11NY4oCIvjw/1.png!thumbnail)  

2、在pom.xml文件中添加两个jar包
      ![beam](https://images-cdn.shimo.im/yjuCQiSMZtwokpVJ/4.png!thumbnail)                                                          

3、新建一个txtIOTest.java

![beam](https://images-cdn.shimo.im/KnSbxGwU6kQbefoX/5.png!thumbnail) 

写入以下代码：

public static void main(String[] args) {
PipelineOptions pipelineOptions = PipelineOptionsFactory.create();//创建管道工厂
pipelineOptions.setRunner(DirectRunner.class);//设置运行的模型，现在一共有3种
// DataflowPipelineOptions dataflowOptions=pipelineOptions.as(DataflowPipelineOptions.class);
// dataflowOptions.setRunner(DataflowRunner.class);
Pipeline pipeline = Pipeline.create(pipelineOptions);//设置相应的管道
//根据文件路径读取文件内容
pipeline.apply(TextIO.read().from("/usr/local/text.txt")).apply("ExtractWords", ParDo.of(new DoFn<String, String>()
 {
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

4、因为Windows上的Beam2.0.0不支持本地路径，需要部署到Linux上，需要打包如图，此处注意要把依赖jar都打包进去。

![beam](https://images-cdn.shimo.im/Gn7d0l5Js5Mi1bJC/6.png!thumbnail) 
 


5、部署beam.jar到Linux 环境中

使用 Xshell 5 登录虚拟机或者Linux系统。用rz 命令把刚才打包的文件上传上去。其中虚拟机要安装上jdk，环境变量要配置好。

我们可以用输入javac命令测试一下。
![beam](https://images-cdn.shimo.im/GSYmedIrxz4uKCOb/7.png!thumbnail) 


我们把beam.jar上传到/usr/local/ 目录下面，然后新建一个文件也就是源文件。
命令：touch  text.txt　
命令：chmod o+rwx　text.txt
![beam](https://images-cdn.shimo.im/rgY1vnEUzC0rT865/8.png!thumbnail) 
修改text.txt并添加数据。　
命令：vi text.txt

![beam](https://images-cdn.shimo.im/q7Fw1ZGyhs0DUvmX/9.png!thumbnail) 


　
运行命令：java -jar beam.jar，生成文件。

![beam](https://images-cdn.shimo.im/o9MxeA3wTfEGChKE/10.png!thumbnail)

用cat 命令查看文件内容，里面就是统计的结果。

![beam](https://images-cdn.shimo.im/JjdNapmHpOUdcE5K/11.png!thumbnail)

*8.3 实战剖析

根据以上的 实战，我们可以从代码看出Beam的运用原理。

第一件事情就是搭建一个管道（Pipeline），例如我们小时候家里浇地用的“水管”。它就是连接水源和处理的桥梁。

PipelineOptions pipelineOptions = PipelineOptionsFactory.create();//创建管道.

第二件事情就是让我们的管道有一个处理的框架，也就是我们的Runtimes  。例如我们接到水要怎么处理，是输送给我们城市的污水处理厂，还是其他。这个污水处理厂就相当于我们的处理框架，例如现在流行的，Apache Spark 或 Apache Flink。其实这个要根据自己的业务指定了。例如以下代码我指定了本地的处理框架。
pipelineOptions.setRunner(DirectRunner.class);

第三件事情也就是我们Beam 最后的一个重要的地方就是我们的模型 (Model), 通俗点讲就是我们的数据来源，如果结合以上第一件和第二件的事情说就是水从那里来，水的来源，可能是河里、可能是污水通道等等。他有多个，本实例用的是有界固定大小的文本文件。当然Model 还有无界的例如kafka等等，可以根据的需求灵活运用。

pipeline.apply(TextIO.read().from("/usr/local/text.txt")).apply("ExtractWords", ParDo.of(new DoFn<String, String>()

最后一个就是处理结果，这个就比较简单了，可以根据自己的需求去处理。通过代码的实战结合原理剖析让大家更快的熟悉Beam ，去简单运用Beam.

#九．总结
----------
Apache Beam 是集成了很多数据模型的一个统一化平台，它为大数据开发工程师频繁换数据源或多数据源、多计算框架提供了集成统一框架平台。Apache Beam社区现在已经集成了数据库的切换IO，未来中文Beam社区还将为Beam集成更多的model和计算框架，为大家提供方便。

张海涛，目前就职于海康威视云基础平台，负责云计算大数据的基础架构设计和中间件的开发，专注云计算大数据方向。Apache Beam 中文社区发起人之一，如果想进一步了解最新 Apache Beam 动态和技术研究成果，请加微信 cyrjkj 入群共同研究和运用。


