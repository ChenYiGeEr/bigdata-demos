package com.lim.demos.spark.day06

import org.apache.spark.sql.SparkSession
import org.junit.Test

class $04_SparkSql {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("sparkSessionTest")
      .master("local")
      .getOrCreate()
  import sparkSession.implicits._

  /**
   * SparlSQL 编程方式有两种
   *  1. 命令式：使用方法操作数据
   *  2. 声明式：使用SQL语句操作数据
   * */

  /** 命令式：使用方法操作数据 */
  @Test
  def operateByCommand(): Unit = {
    val list = List(
      Person("李铭", 27, "男"),
      Person("李铭", 28, "男"),
      Person("马萌萌", 30, "女"),
      Person("李政", 21, "男")
    )
    val df = list.toDF()
    // 列裁剪
    df.selectExpr("name", "age + 10")
    // 过滤
    .where("age < 30")
    // 去重
//    .distinct()
    // 根据某个字段去重
    .dropDuplicates("name")
    .show()
  }


  /** 声明式：使用SQL语句操作数据 */
  @Test
  def operateByStatement(): Unit = {
    val list = List(
      Person("李铭", 27, "男"),
      Person("李铭", 28, "男"),
      Person("马萌萌", 30, "女"),
      Person("李政", 21, "男")
    )
    // 把DF注册成表 表名为person
    list
      .toDF()
      .createOrReplaceTempView("person")
    sparkSession.sql(
      """
        |select
        | distinct
        | name, age
        | from person
        | where age < 30
        | order by age desc
        |""".stripMargin
    ).show
  }
}
