package com.lim.demos.spark.day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/** RDD 分区 */
class $02_RddPartition {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[*]")
      // 设置分区默认值
      .set("spark.default.parallelism", "7")
      .setAppName("createRddByLocalCollection")
  )

  /** 创建RDD的分区的方式 */


  /**
   * 1. 通过本地集合创建RDD，RDD的分区数 一般用于测试环境模拟生产环境
   *  1. sc.parallelize(集合，分区数) 可以设置分区数，优先级大于 spark.default.parallelism
   *  2. 若没有通过 spark.default.parallelism 设置分区数
   *    sc.makeRDD 此时分区数为默认值 = 10
   *
   * (0 until 3).iterator.map { i =>
   *    val start = ((i * length) / 3).toInt
   *    val end = (((i + 1) * length) / 3).toInt
   *    (start, end)
   * }
   * */
  @Test
  def createRddByLocalCollection(): Unit = {
    // 查看RDD的分区数 10
    println(sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9)).getNumPartitions)
    println("______________________________________")
    println(sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3).getNumPartitions)
  }

  /**
   * 2. 通过读取文件创建RDD，RDD的分区数 => 2
   *  1. 读取文件创建的RDD的分区数 = min (spark.default.parallelism, 2)
   *  分区数最终由文件切片个数决定
   * */
  @Test
  def createRddByReadFile(): Unit = {
    val rdd = sc.textFile("datas/session.txt", 4)
    println(rdd.getNumPartitions)
    println("______________________________________")
    val rdd1 = sc.textFile("hdfs://hadoop102:8020/user/hive/warehouse/user_behavior/session.txt")
    println(rdd1.getNumPartitions)
  }

  /**
   * 3. 通过其它RDD创建的RDD的分区数 = 依赖的第一个RDD的分区数
   * */
  @Test
  def createRddByOtherRdd(): Unit = {
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 3)
    println(rdd.getNumPartitions)
    // 通过其它RDD创建
    val rdd1: RDD[Int] = rdd.sortBy(x => x)
    println(rdd1.getNumPartitions)
  }

}
