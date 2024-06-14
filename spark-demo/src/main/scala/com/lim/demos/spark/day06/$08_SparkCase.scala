package com.lim.demos.spark.day06

import org.apache.spark.sql.SparkSession

object $08_SparkCase {

  // 创建SparkSession
  val sparkSession: SparkSession =
    SparkSession
      .builder()
      .appName("SparkCaseTest")
      .master("local")
      .getOrCreate()

  import sparkSession.implicits._
  // 注册自定义UDF函数
  sparkSession.udf.register("contains", contains _)

  def main(args: Array[String]): Unit = {
    val datas =
      sparkSession
        .read
        .csv("datas/双十一淘宝美妆数据.csv")
        .toDF(
          "update_time",
          "id,",
          "title",
          "price",
          "sale_count",
          "comment_count",
          "store_name"
        )
    datas.createOrReplaceTempView("sales")
    val typesDS =
      sparkSession
        .read
        .textFile("datas/type.txt")
        .flatMap(line => {
        val splits = line.split("\t")
        val mainType = splits(0)
        val subTypes = splits.tail.tail
        subTypes.map((mainType, _))
      })
      .toDF("main_type", "sub_type")
    typesDS.createOrReplaceTempView("s_type")
    sparkSession.sql(
      """
        |select
        | t.main_type,
        | t.sub_type,
        | s.title,
        | s.sale_count,
        | row_number() over (partition by s.title order by s.sale_count) rn
        |from sales s
        |join s_type t on contains(s.title, t.sub_type)
        |""".stripMargin
    ).createOrReplaceTempView("s_tmp1")
    sparkSession.sql(
      """
        |select
        | sub_type,
        | sum(sale_count) as type_sale_count
        |from s_tmp1
        | where rn = 1 and sale_count is not null
        |group by sub_type
        |""".stripMargin
    ).createOrReplaceTempView("s_tmp2")

    sparkSession.sql(
      """
        |select
        | sub_type,
        | type_sale_count,
        | sum(type_sale_count) over () sum_sale_count
        |from s_tmp2
        |""".stripMargin
    ).createOrReplaceTempView("s_tmp3")

    sparkSession.sql(
      """
        |select
        | sub_type,
        | sum_sale_count,
        | type_sale_count,
        | (type_sale_count / sum_sale_count) as sale_count_ratio
        |from s_tmp3
        |""".stripMargin
    ).show()
  }

  def contains(title: String, keyword: String): Boolean = {
    title.contains(keyword)
  }

}
