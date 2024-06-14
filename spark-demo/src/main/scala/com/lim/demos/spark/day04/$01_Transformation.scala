package com.lim.demos.spark.day04

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class $01_Transformation {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("calculateScoreAvg")
  )

  @Test
  def aggregateByKey ():Unit = {
    val rdd1 = sc.textFile("datas/score.txt")
    val rdd2:RDD[(String, Double)] = rdd1.map(line => {
      val splits = line.split(",")
      val name = splits(1)
      val score = splits.last.toDouble
      (name, score)
    })
    // RDD[("语文", (60.0, 2))]
    val rdd3:RDD[(String,(Double, Int))] = rdd2.aggregateByKey(
      // 初始值
      (0.00, 0))(
      (agg, curr) => (agg._1 + curr, agg._2 + 1),
      (agg, curr) => (agg._1 + curr._1, agg._2 + curr._2)
    )

    val rdd4 = rdd3.map({
      case (name, (score, num)) => (name, score / num)
    })

    rdd4.collect().foreach(println)

  }

  @Test
  def sortByKey ():Unit = {
    val rdd = sc.parallelize(List(1, 4, 2, 6, 8, 9, 10))
    val rdd2 = rdd.map(x => (x, null))
    val rdd3 = rdd2.sortBy(ascending = false, f = _._1)
//    val rdd3 = rdd2.sortByKey(ascending = false)
    println(rdd3.collect().toList)
  }

  /**
   * mapValues: 一对一映射，只对value进行操作
   * 针对于(K,V)形式的类型只对V进行操作
   * */
  @Test
  def mapValues(): Unit = {
    val rdd = sc.parallelize(List(("a", 1), ("b", 2), ("c", 3)))
    val rdd3 = rdd.mapValues((x) => x << x)
    println(rdd3.collect().toList)
  }

  @Test
  def join () :Unit ={
    val rdd1 = sc.parallelize(List(("aa", 11), ("bb", 12), ("aa", 13), ("cc", 14)))
    val rdd2 = sc.parallelize(List(("aa", 1.1), ("cc", 2.2), ("dd", 3.3), ("cc", 4.4)))
    // join相当于sql inner join
    val rdd3:RDD[((String), (Int, Double))] = rdd1.join(rdd2)
    println(rdd3.collect().toList)
    val rdd4 = rdd1.leftOuterJoin(rdd2)
    println(rdd4.collect().toList)
  }

  /**
   * cogroup = groupByKey + fullOuterJoin
   * */
  @Test
  def cogroup () :Unit = {

    val rdd1 = sc.parallelize(List(("aa", 11), ("bb", 12), ("aa", 13), ("cc", 14)))
    val rdd2 = sc.parallelize(List(("aa", 1.1), ("cc", 2.2), ("dd", 3.3), ("cc", 4.4)))
    val rdd3 = rdd1.cogroup(rdd2)
    println(rdd3.collect().toList)
  }

}
