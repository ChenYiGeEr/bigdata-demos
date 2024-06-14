package com.lim.demos.spark.day06

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object $02_DataFrame {

  /**
   * DataFrame的四种创建方式
   *  1. 通过toDF方法创建
   *  2. 通过读取文件方法创建
   *  3. 通过其它的DataFrame衍生
   *  4. 通过createDataFrame方法创建
   * */
  def main(args: Array[String]): Unit = {
    // 1. 创建SparkSession
    val sparkSession: SparkSession =
      SparkSession
        .builder()
        .appName("sparkSessionTest")
        .master("local")
        .getOrCreate()
    // 1. 通过toDF方法创建
    val dataFrame = createDataFrameByToDF(sparkSession)
    dataFrame.printSchema()
    dataFrame.show()
    // 2. 通过读取文件方法创建DataFrame
    val dataFrame1 = createDataFrameByReadFile(sparkSession)
    dataFrame1.printSchema()
    dataFrame1.show()
    // 3. 通过其他DataFrame衍生
    val dataFrame2 = createDataFrameByOtherDataFrame(dataFrame1)
    dataFrame2.printSchema()
    dataFrame2.show()
    // 4. 通过createDataFrame方法创建
    val dataFrame3 = createDataFrameByCreateDataFrame(sparkSession)
    dataFrame3.printSchema()
    dataFrame3.show()
  }

  // 1. 通过toDF方法创建DataFrame
  def createDataFrameByToDF (sparkSession: SparkSession) : DataFrame = {
    import sparkSession.implicits._
    val list = List((1, "zhangsan", 20),(2, "lisi", 21),(3, "wangwu", 22))
    list.toDF("id", "name", "age")
    val list1 = List( Person("李铭",27,"男"), Person("马萌萌",30,"女") )
    list1.toDF();
  }

  // 2. 通过读取文件方法创建DataFrame
  def createDataFrameByReadFile(sparkSession: SparkSession): DataFrame = {
    sparkSession.read.json("datas/json/test.json")
  }

  // 3.通过其他DataFrame衍生
  def createDataFrameByOtherDataFrame(dataFrame: DataFrame): DataFrame = {
    dataFrame.filter("gender = '男'")
  }

  // 4. 通过createDataFrame方法创建
  def createDataFrameByCreateDataFrame(sparkSession: SparkSession): DataFrame = {
    val fields = Array(StructField("name", StringType), StructField("age", IntegerType), StructField("gender", StringType))
    val schema = StructType(fields)
    val rdd = sparkSession.sparkContext.parallelize(List(Row("李铭", 27, "男"), Row("马萌萌", 30, "女")));
    sparkSession.createDataFrame(rdd, schema)
  }


}

case class Person (var name: String, var age: Int, var gender: String)
