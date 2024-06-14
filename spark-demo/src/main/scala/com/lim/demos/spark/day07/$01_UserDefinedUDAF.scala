package com.lim.demos.spark.day07

import org.apache.spark.sql.{SparkSession, functions}

object $01_UserDefinedUDAF {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("userDefinedUDAFTest")
      .master("local")
      .getOrCreate()

  import sparkSession.implicits._

  def main(args: Array[String]): Unit = {
    var list = List(
      ("1001", "lisi1", 21),
      ("000011", "lisi2", 22),
      ("11112", "lisi3", 25),
      ("00305", "lisi4", 28),
      ("506", "lisi5", 30),
      ("00333", "lisi6", 22),
    )
    val df = list.toDF("id", "name", "age")
    // 需求：员工id正常为0位，若不足8位则左侧使用0补齐
    df.createOrReplaceTempView("employee")
    val ds = df.as[(String, String, Int)]
    // 注册自定义弱类型UDF函数
    // sparkSession.udf.register("customAvg", new WeakReferenceUDAF)
    // 注册自定义强类型UDF函数
     sparkSession.udf.register("customAvg", functions.udaf(new StrongReferenceUDAF))

    // 使用hive自带方法实现以上需求
    // ds.selectExpr("lpad(id, 8, '0') as id", "name", "age").show
    // 使用自定义方法实现以上需求
    ds.selectExpr("customAvg(age)  as avg_age").show
  }

}
