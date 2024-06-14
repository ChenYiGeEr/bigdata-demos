package com.lim.demos.spark.day06

import org.apache.spark.sql.SparkSession

object $01_SparkSession {

  def main(args: Array[String]): Unit = {
    // 创建SparkSession
    val sparkSession: SparkSession =
                      SparkSession
                        .builder()
                        .appName("sparkSessionTest")
                        .master("local")
                        .getOrCreate()
  }

}
