package com.lim.demos.spark.day05

import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}

object $02_Partitioner {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("checkPointTest")
  )

  /**
   * Partitioner 分区器
   *  HashPartitioner
   *    分区规则：hashCode%分区数
   *      如果hashCode % 分区数 < 0 + 分区数
   *        hashCode % 分区数 >= 0
   *  RangePartitioner
   *    分区规则：
   *    1. 对RDD数据进行抽样，确定分区数-1个key
   *    2. 使用获取的key确定每个分区的数据辩解
   *    3. 后续使用每个数据的key与分区边界对比，如果在边界内则将该数据放入该边界中
   * */

  def main(args: Array[String]): Unit = {
    val rdd = sc.parallelize(List(1, 4, 2, 7, 9, 22, 55, 6, 10, 13, 12, 10, 6))
    val rdd2= rdd.map(x => (x, null))
    val rdd3 = rdd2.partitionBy(new RangePartitioner(5, rdd2))
    rdd3.mapPartitionsWithIndex((index, it) => {
      println(s"index: ${index}, iterator: ${it.toList}")
      it
    }).collect()
  }
}
