package com.lim.demos.spark.day04

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class $03_Action {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("testAction")
  )

  /**
   * collect: 收集RDD每个分区的数据以数据封装之后发给Driver
   *  Driver内存默认1G 可能会出现内存溢出情况，工作中一般需要将Driver内存设置为5~10G
   *  可以通过bin/spark-submit -driver-memory 10G进行设置
   * */
  @Test
  def collect() : Unit = {
    val rdd = sc.parallelize(List(1,2,3))
    val rdd1 = rdd.collect()
    println(rdd1)
  }

  @Test
  def count(): Unit = {
    val rdd = sc.parallelize(List(1, 2, 3))
    println(rdd.count)
  }

  /**
   * .first 获取0号分区中的第一个元素
   * 可能会产生多个job,最多产生2个job
   * */
  @Test
  def first(): Unit = {
    val rdd = sc.parallelize(List(1, 2, 3))
    println(rdd.first)
  }

  /**
   * .take RDD前n个元素
   * */
  @Test
  def take(): Unit = {
    val rdd = sc.parallelize(List(1, 2, 3), 3)
    println(rdd.take(3).toList)
  }

  /**
   * .takeOrdered 将RDD排序后取前n个元素
   * 只会产生1个job 不会产生shuffle
   * */
  @Test
  def takeOrdered (): Unit = {
    val rdd = sc.parallelize(List(3, 2, 1, 41, 31), 3)
    println(rdd.takeOrdered(3).toList)
  }

  /**
   * .countByKey 统计key出现的个数
   * mapValues + reduceByKey
   * */
  @Test
  def countByKey(): Unit = {
    val rdd = sc.parallelize(List(("a",1),("a",2),("b",2)), 3)
    println(rdd.countByKey().toList)
  }

  /**
   *
   */
  @Test
  def save(): Unit = {
    val rdd = sc.parallelize(List(("a", 1), ("a", 2), ("b", 2)), 3)
    rdd.saveAsTextFile("datas/output")
  }

  @Test
  def foreach(): Unit = {
    val rdd = sc.parallelize(List(("a", 1), ("a", 2), ("b", 2)), 3)
    rdd.foreach(println)
  }

  /**
   * 针对RDD中的分区进行遍历
   * 使用场景：一般用于将数据写入mysql/hbase/redis等位置，可以减少连接的创建和销毁的次数
   * */
  @Test
  def foreachPartition: Unit = {
    val rdd = sc.parallelize(List(("a", 1), ("a", 2), ("b", 2)), 3)
    rdd.foreachPartition(it => {
      it.foreach(println)
    })
  }

}
