package com.lim.demos.spark.day04

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

class $02_Test {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("ProvinceAdvertisingAnalysis")
  )

  /** 统计每个省份点击量Top3的广告 */
  @Test
  def test(): Unit = {
    // 1. 读取数据
    val rdd = sc.textFile("datas/agent.log")
    // 2. 之前方案处理
//    solveOld(rdd, 3)
    // 3. 新的处理方案
    solveNew(rdd, 3)
  }

  def solveOld(rdd: RDD[String], topN: Int): Unit = {
    // 2. 过滤 不需要
    // 3. 裁剪
    val result =
    rdd
      .map(line => {
        val splits = line.split(" ")
        // 省份编号
        val provinceId = splits(1)
        // 广告编号
        val adsId = splits.last
        // （省份id，（广告id，出现了一次））
        (provinceId, (adsId, 1))
      })
      // 4. 去重 不需要
      // 5. 分组，可能会产生数据倾斜
      // 解决方案 自定义分区器
      .groupByKey()
      // 6.统计
      // ("河南省"，(("A", 1), "B", "A", "C))
      // ("河南省", List("A" => 12, "B" =>10))
      .map(item => {
        val tuples = item._2
          .groupBy(_._1)
          .mapValues(_.size)
          .toList
          .sortBy(_._2)
          .reverse
          .take(topN)
        (item._1, tuples)
      })
      .collect().toList
    // 打印输出
    result.foreach(println)

  }

  def solveNew(rdd: RDD[String], topN: Int): Unit = {
    // 2. 过滤 不需要
    // 3. 裁剪
    val result =
    rdd
      .map(line => {
        val splits = line.split(" ")
        // 省份编号
        val provinceId = splits(1)
        // 广告编号
        val adsId = splits.last
        //（（省份id，广告id），出现了一次）
        ((provinceId, adsId), 1)
      })
      // 4. 去重 不需要
      // 5. 分组，
      .reduceByKey((agg, cur) => {
        agg + cur
      })
      .groupBy({
        case ((provinceId, adsId), num) => provinceId
      })
      .map(item => {
        val tuples = item._2
                      .map({
                        case ((province, adsId), num) => (adsId, num)
                      })
                      .toList
                      .sortBy(_._2)
                      .reverse
                      .take(topN)
        (item._1, tuples)
      })
      // 6.统计
      .collect().toList
    // 打印输出
    result.foreach(println)
  }
}
