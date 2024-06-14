package com.lim.demos.spark.day03

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class $01_Transformation {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("calculateScoreAvg")
  )

  @Test
  def test ():Unit = {
    // 统计score表中学科平均分
    val rdd1 = sc.textFile("datas/score.txt")
    // (语文,(20, 1)),(语文,(30, 1)),(语文, (40, 1))
    // 语文 -> 30
    rdd1
      .map(line => {
        val splits = line.split(",")
        (splits(1), (splits(2).toDouble, 1))
      })
      // 使用reduceByKey方法实现对科目求平均值
      .reduceByKey((tmp, curr) => {
        (tmp._1 + curr._1, tmp._2 + curr._2)
      })
      .map(x => {
        (x._1, x._2._1 / x._2._2)
      })
//      .map({
//        case (subject, (totalScore, count)) => (subject, totalScore / count)
//      })
      .collect()
      .foreach(println)
  }

}
