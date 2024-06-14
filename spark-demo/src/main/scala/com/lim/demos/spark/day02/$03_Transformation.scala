package com.lim.demos.spark.day02

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * RDD算子
 *  1, transformation 转换算子：会生成新的RDD，不会触发任务的计算
 *  1, action 行动算子：不会生成RDD，会触发任务的计算
 * */
class $03_Transformation {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[*]")
      .setAppName("createRddByLocalCollection")
  )

  /**
   * map(func: RDD元素类型 => B)：一对一映射
   *  map的函数是针对RDD每个元素的操作，元素有多少个就指定多少次函数
   *
   * */
  @Test
  def mapTransformer (): Unit = {
    val rdd = sc.parallelize(List(1, 4, 3, 6, 9, 10), 3)
    val rdd2 = rdd.map(x => {
      println(s"${Thread.currentThread().getName} -- ${x}")
      x * x
    })
    println(rdd2.collect().toList)
  }

  @Test
  def flatMapTransformer (): Unit = {
    val rdd = sc.parallelize(List("aa bb cc", "dd ee ff" , "gg ll mmm nn"), 3)
    val rdd1 = rdd.flatMap(x => {
      println(s"${Thread.currentThread().getName} -- ${x}")
      x.split(" ")
    })
    println(rdd1.collect().toList)
  }

  @Test
  def filterTransformer(): Unit = {
    val rdd = sc.parallelize(List(1, 4, 3, 6, 9, 10), 3)
    val rdd2 = rdd.filter(x => {
      println(s"${Thread.currentThread().getName} -- ${x}")
      x % 2 == 0
    })
    println(rdd2.collect().toList)
  }

}
