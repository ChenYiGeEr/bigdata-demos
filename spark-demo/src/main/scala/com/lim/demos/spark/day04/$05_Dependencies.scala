package com.lim.demos.spark.day04

import org.apache.spark.{SparkConf, SparkContext}

object $05_Dependencies {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("dependenciesTest")
  )

  /**
   *
   * 查看血统 rdd.toDebugString
   * 查看依赖 rdd.dependencies
   *  依赖分为两种：宽依赖和窄依赖
   *   有shuffle的为宽依赖
   *   没有shuffle的为窄依赖
   *
   * Application:应用
   *  Job:任务（一般一个action算子产生一个job，first、take可能会会产生多个job）
   *    Stage:阶段 阶段个数 = shuffle个数 + 1（一个job会被切分成多个stage，切分的依据是宽依赖）
   *    Task: 子任务 一个阶段中的task个数 = stage中最后一个RDD的分区数
   * 一个Application中多个job是串行的
   * 一个job中多个stage是串行的
   * 一个stage中多个task是并行的
   * */
  def main(args: Array[String]): Unit = {
    val rdd = sc.textFile("datas/score.txt")
    println("rdd血统：" + rdd.toDebugString)
    println("______________________________")
    println("rdd依赖：" + rdd.dependencies.toList)
    val rdd1 = rdd.map(x =>{
      val splits = x.split(",")
      val stuName = splits(0)
      val projectName = splits(1)
      val score = splits(2).toDouble
      (stuName, projectName, score)
    })
    println("rdd1血统：" + rdd1.toDebugString)
    println("______________________________")
    println("rdd1依赖：" + rdd1.dependencies.toList)
    val rdd2 = rdd1.sortBy(_._3)
    println("rdd2血统：" + rdd2.toDebugString)
    println("______________________________")
    println("rdd2依赖：" + rdd2.dependencies.toList)
    println(rdd2.collect().toList)
  }

}