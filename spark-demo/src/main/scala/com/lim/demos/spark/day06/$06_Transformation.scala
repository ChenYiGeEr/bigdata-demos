package com.lim.demos.spark.day06

import org.apache.spark.sql.SparkSession

object $06_Transformation {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("sparkSessionTest")
      .master("local")
      .getOrCreate()
  import sparkSession.implicits._

  def main(args: Array[String]): Unit = {
    val list = List((1, "zhangsan", 20),(2, "lisi", 21),(3, "wangwu", 22))
    val listRdd = sparkSession.sparkContext.parallelize(list)
    // RDD -> DataFrame
    listRdd.toDF()
    // RDD -> Dataset
    listRdd.toDS()

    // DataFrame -> RDD
    listRdd.toDF().rdd.map( row => {
//      row.getAs[Int](3)
      row.getAs[Int]("name")
    })
    // DataFrame -> Dataset
    val ds = listRdd.toDF("id", "name", "age").as[(Int, String, Int)]
    ds.show
    // Dataset -> RDD/DataFrame
    listRdd.toDS().rdd.map(_._2)
    // Dataset -> DataFrame
    listRdd.toDS().toDF()
  }
}
