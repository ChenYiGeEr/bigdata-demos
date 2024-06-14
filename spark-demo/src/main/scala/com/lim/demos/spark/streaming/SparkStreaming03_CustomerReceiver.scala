package com.lim.demos.spark.streaming

import com.lim.demos.spark.streaming.receiver.CustomerReceiver
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * SparkStreaming03_CustomerReceiver
 * 自定义的数据源采集数据
 * */
object SparkStreaming03_CustomerReceiver {

  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置信息
    val sparkConf = new SparkConf()
                      .setAppName("SparkStreaming03_CustomerReceiver")
                      .setMaster("local[*]")
    //2.初始化SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //3.创建自定义receiver的Streaming
    val lineDStream = ssc.receiverStream(new CustomerReceiver("hadoop102", 9999))

    //4.将每一行数据做切分，形成一个个单词
    val wordDStream = lineDStream.flatMap(_.split(" "))

    //5.将单词映射成元组（word,1）
    val wordToOneDStream = wordDStream.map((_, 1))

    //6.将相同的单词次数做统计
    val wordToSumDStream = wordToOneDStream.reduceByKey(_ + _)

    //7.打印
    wordToSumDStream.print()

    //8.启动SparkStreamingContext
    ssc.start()
    ssc.awaitTermination()
  }



}
