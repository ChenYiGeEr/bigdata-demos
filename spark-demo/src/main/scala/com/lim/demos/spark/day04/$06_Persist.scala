package com.lim.demos.spark.day04

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD的持久化
 * 应用场景
 *  1,一个Rdd在多个job中重复使用，每个job执行时该RDD之前的处理步骤也会重复执行
 *  2,如果一个job的依赖链条很长，如果计算出错或数据丢失需要重新从头计算 浪费大量时间
 * 存储时机  在缓存所在第一个job执行过程中
 * 持久化方式
 *  1,cache 存储位置 在分区所在主机的内存中
 *  2,persist 存储位置 在分区所在主机的内存/磁盘中
 *            工作中常用的存储级别 StorageLevel.MEMORY_ONLY(数据只保存在内存中，适用于数据量较小的情况下)
 *                             StorageLevel.MEMORY_AND_DISK(数据保存在内存和磁盘中，适用于数据量大的情况下)
 *
 * */
object $06_Persist {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("dependenciesTest")
  )

  def main(args: Array[String]): Unit = {
    val rdd = sc.textFile("datas/score.txt")
    val rdd1 = rdd.map(x => {
      val splits = x.split(",")
      val stuName = splits(0)
      val projectName = splits(1)
      val score = splits(2).toDouble
      (stuName, projectName, score)
    })
    // 数据缓存底层调用的就是persist方法,缓存级别默认用的是MEMORY_ONLY
    rdd1.cache()
    // persist方法可以更改存储级别
    rdd1.persist(StorageLevel.MEMORY_AND_DISK)
    val rdd2 = rdd1.sortBy(_._3)
    println(rdd2.collect().toList)
    rdd1.unpersist();
  }

}
