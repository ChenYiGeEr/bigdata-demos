package com.lim.demos.spark.day06

import org.apache.spark.sql.{Dataset, SparkSession}
import org.junit.Test

class $05_Test {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("sparkSessionTest")
      .master("local")
      .getOrCreate()
  import sparkSession.implicits._

  @Test
  def test():Unit = {
    val ds:Dataset[String] = sparkSession.read.textFile("datas/wc.txt")
    ds.flatMap(_.split(" "))
      .toDF("word")
      .createOrReplaceTempView("word_count")
    sparkSession.sql(
      """
        |select
        | word,
        | count(word) as show_times
        |from word_count
        |group by word
        |order by show_times desc
        |""".stripMargin)
    .show
  }
  
}
