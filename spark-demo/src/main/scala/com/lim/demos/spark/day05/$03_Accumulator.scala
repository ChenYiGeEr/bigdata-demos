package com.lim.demos.spark.day05

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.collection.mutable

/**
 * 累加器
 *  使用场景
 *    一般用于聚合场景 并 最终聚合结果不能太大 ，聚合结构不超过Driver的内存大小 5~10G
 *  好处
 *    一定程度上可以减少shuffle操作
 * */
class $03_Accumulator {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("checkPointTest")
  )

  @Test
  def longAccumulator(): Unit = {
    val sumAcc = sc.longAccumulator("sumAcc")
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    rdd.foreach(a => sumAcc.add(a))
    println(sumAcc.value)
  }
  @Test
  def collectionAccumulator(): Unit = {
    val collectAcc = sc.collectionAccumulator[mutable.Map[String, Int]]("collectionAcc")
    val rdd = sc.textFile("datas/wc.txt")
    val rdd1 = rdd.flatMap(_.split(" "))
    val rdd2 = rdd1.map((_,1))
    rdd2.foreachPartition(iter => {
      val map = mutable.Map[String, Int]()
      iter.foreach(t => {
        val key = t._1
        val value = t._2
        map.put(key, map.getOrElse(key, 0) + value)
      })
      collectAcc.add(map)
    })
    // 隐式转换 将java集合转换为scala集合
    import scala.collection.JavaConverters._
    val flatten = collectAcc.value.asScala.flatten
    val groupByRes = flatten.groupBy(_._1)
    val mapRes = groupByRes.map(x => {
      val z = x._2.map(y => y._2)
      (x._1, z.sum)
    })
    // val rdd3 = rdd2.reduceByKey((x, y) => x + y)
    // val rdd4 = rdd3.sortBy(_._2, ascending = false)
    // println(rdd4.collect().toList)
    println(mapRes.toList.sortBy(_._2).reverse)
  }

}
