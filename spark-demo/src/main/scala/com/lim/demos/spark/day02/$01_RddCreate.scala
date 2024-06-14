package com.lim.demos.spark.day02

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/** RDD的三种创建方式 */
class $01_RddCreate {

  /**
   * 1. 通过本地集合创建 一般用于测试环境模拟生产环境
   * */
  @Test
  def createRddByLocalCollection(): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("createRddByLocalCollection")
    )
    sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9))
      .collect()
      .foreach(println)
    println("______________________________________")
    sc.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9))
      .collect()
      .foreach(println)
  }

  /**
   * 2. 通过读取文件创建RDD 一般用于学习环境
   * */
  @Test
  def createRddByReadFile(): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("createRddByLocalCollection")
    )
    val rdd = sc.textFile("datas/session.txt")
    rdd.collect()
      .foreach(println)
    println("______________________________________")
    val rdd1 = sc.textFile("hdfs://hadoop102:8020/user/hive/warehouse/user_behavior/session.txt")
    rdd1.collect()
      .foreach(println)
  }

  /**
   * 3. 通过其它RDD创建 生产环境中使用
   * */
  @Test
  def createRddByOtherRdd(): Unit = {
    val sc = new SparkContext(
      new SparkConf()
        .setMaster("local[*]")
        .setAppName("createRddByLocalCollection")
    )
    val rdd:RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9))
    // 通过其它RDD创建
    val rdd1:RDD[Int] = rdd.sortBy(x => x)
  }

}

