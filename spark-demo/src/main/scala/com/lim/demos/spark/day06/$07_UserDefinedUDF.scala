package com.lim.demos.spark.day06

import org.apache.spark.sql.SparkSession

object $07_UserDefinedUDF {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("userDefinedUDFTest")
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
    // 注册自定义UDF函数
    sparkSession.udf.register("customLpad", customLPAD _)
    // 使用hive自带方法实现以上需求
//    ds.selectExpr("lpad(id, 8, '0') as id", "name", "age").show
    // 使用自定义方法实现以上需求
    ds.selectExpr("customLpad(id, 8, '0') as id", "name", "age").show
//    sparkSession.sql(
//      """
//        |select
//        | lpad(id, 8, '0') as id,
//        | name,
//        | age
//        |from employee
//        |""".stripMargin
//    ).show
  }

  /**
   * 1. 自定义函数
   * 2. 需将方法注册进SparkSession
   * @param str 需要补充的字符串
   * @param length 长度
   * @param pad 补充用的字符
   * */
  def customLPAD(str: String, length: Int, pad: String): String = {
    s"${pad * (length - str.length)}${str}"
  }
}
