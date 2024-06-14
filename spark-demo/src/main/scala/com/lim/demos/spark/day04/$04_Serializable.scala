package com.lim.demos.spark.day04

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.beans.BeanProperty

class $04_Serializable {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .set("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .setAppName("ProvinceAdvertisingAnalysis")
  )

  @Test
  def test(): Unit = {
    val rdd = sc.parallelize(List(1,2,3))
    val x:Int = 10
    val rdd2 = rdd.map(_ * x)
    println(rdd2.collect().toList)
  }
}


/**
 * 默认使用
 *  Java序列化：会将类的继承信息、类的属性值、类的属性类型、全类名等全部序列化进去
 * 工作中使用
 *  Kryo序列化：只会序列化类的属性、属性类型、全类型
 *  Kryo序列化会比Java序列化快10倍左右
 * 如何设置spark序列化方式
 *  1. SparkConf中设置“spark.serializer”为“org.apache.spark.serializer.KryoSerializer”
 *  2. 注册哪些类使用Kryo序列化（可选）：new SparkConf().registerKryoClasses(Array(classOf[类名]))
 * */
// extends Serializable 继承序列化
class Person extends Serializable {
  @BeanProperty
  var name: String = _
  var age: Int = _
}