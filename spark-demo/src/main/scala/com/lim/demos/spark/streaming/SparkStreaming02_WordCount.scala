package com.lim.demos.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * SparkStreaming
 * WordCount 统计单词出现次数
 * */
object SparkStreaming02_WordCount {

  /** 主函数 方法入口 */
  def main(args: Array[String]): Unit = {
    // 1. 初始化Spark配置信息
    val sparkConf = new SparkConf()
                      .setAppName("SparkStreaming02_WordCount")
                      .setMaster("local[*]")
    // 2. 初始化 SparkStreamingContext
    val ssc = new StreamingContext(sparkConf, Seconds(4))
    //3.创建RDD队列
    val rddQueue = new mutable.Queue[RDD[Int]]()
    //4.创建QueueInputDStream
    // oneAtATime = true    默认，一次读取队列里面的一个数据
    // oneAtATime = false， 按照设定的批次时间，读取队列里面数据
    val inputDStream = ssc.queueStream(rddQueue, oneAtATime = false)
    //5.处理队列中的RDD数据
    val sumDStream = inputDStream.reduce(_ + _)

    //6.打印结果
    sumDStream.print()

    //7.启动任务
    ssc.start()

    //8.循环创建并向RDD队列中放入RDD
    for (i <- 1 to 5) {
      rddQueue += ssc.sparkContext.makeRDD(1 to 5)
      Thread.sleep(2000)
    }

    ssc.awaitTermination()
  }

}
