package com.lim.demos.spark.day06

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object $03_DataSet {

  /**
   * DataSet的四种创建方式
   *  1. 通过toDS方法创建
   *  2. 通过读取文件创建
   *  3. 通过其它DataSet衍生
   *  4. 通过createDataSet方法创建
   * */
  def main(args: Array[String]): Unit = {
    // 1. 创建SparkSession
    val sparkSession: SparkSession =
                  SparkSession
                    .builder()
                    .appName("sparkSessionTest")
                    .master("local")
                    .getOrCreate()
    // 1. 通过toDS方法创建
    val dataSet = createDataSetByToDS(sparkSession)
    dataSet.printSchema()
    dataSet.show()
    // 2. 通过读取文件创建
    val dataSet1 = createDataSetByReadFile(sparkSession)
    dataSet1.printSchema()
    dataSet1.show()
    // 3. 通过其它DataSet方法创建
    val dataSet2 = createDataSetByOtherDataSet(dataSet1)
    dataSet2.printSchema()
    dataSet2.show()
    // 4. 通过createDataSet方法创建
    val dataSet3 = createDataSetByCreateDataSet(sparkSession)
    dataSet3.printSchema()
    dataSet3.show()
  }

  // 1. 通过toDS方法创建
  def createDataSetByToDS(sparkSession: SparkSession): DataFrame = {
    import sparkSession.implicits._
    val list = List((1, "zhangsan", 20), (2, "lisi", 21), (3, "wangwu", 22))
    list.toDS()
        .toDF("id", "name", "age")
//    val list1 = List( Person("李铭",27,"男"), Person("马萌萌",30,"女") )
//    list1.toDS()
  }

  // 2. 通过读取文件创建
  def createDataSetByReadFile(sparkSession: SparkSession): Dataset[(String, Int)] = {
    import sparkSession.implicits._
    val dataset = sparkSession.read.textFile("datas/wc.txt")
    dataset
      .flatMap(x => x.split(" "))
      .map((_, 1))
  }

  // 3. 通过其它DataSet方法创建
  def createDataSetByOtherDataSet(dataset:Dataset[(String, Int)]): Dataset[(String, Int)] = {
    dataset.filter(str => {
      str._1.startsWith("hell")
    })
  }

  // 4. 通过createDataSet方法创建
  def createDataSetByCreateDataSet (sparkSession: SparkSession) : Dataset[String] = {
    import sparkSession.implicits._
    sparkSession.createDataset(List("hello", "world"))
  }
}