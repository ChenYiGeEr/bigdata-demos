package com.lim.demos.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * SparkStreaming
 * WordCount 统计单词出现次数
 * */
object SparkStreaming01_WordCount {
  /** 主函数 方法入口 */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置信息
    val sparkConf = new SparkConf()
                      .setAppName("SparkStreaming01_WordCount")
                      .setMaster("local[*]")
    // 2. 初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    // 3. 通过监控端口创建DStream，读进来的数据为一行行
    val lineDStream = ssc.socketTextStream("hadoop102", 9999)
    // 3.1 将每一行的数据进行切分，形成一个个单词
    val wordDStream = lineDStream.flatMap(_.split(" "))
    // 3.2 将单词映射成元组
    val wordToOneDStream = wordDStream.map((_, 1))
    // 3.3 将相同的单子次数做统计
    val wordToSumDStream = wordToOneDStream.reduceByKey(_ + _)
    // 3.4 打印
    wordToSumDStream.print()
    // 4 启动SparkStreamingContext
    ssc.start()
    // 将主线程阻塞，主线程不退出
    ssc.awaitTermination()
  }

}
