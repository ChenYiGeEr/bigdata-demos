package com.lim.demos.spark.day07

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object $04_SparkHive {

  /**
   * idea操作hive
   * 1. 导入hive依赖
   *  1.1 spark-hive_2.12(3.1.3)
   *  1.2 mysql-connector-java(5.1.31)
   * 2. 在resource中引入hive-site.xml配置文件
   * 3. 创建SparkSession时候开启hive支持
   * 4. 操作hive
   * */
  def main(args: Array[String]): Unit = {
    // 配置环境变量HADOOP_USER_NAME
    System.setProperty("HADOOP_USER_NAME", "parallels")
    val sparkSession = SparkSession
                        .builder()
                        // spark开启hive支持
                        .enableHiveSupport()
                        .config(
                          new SparkConf()
                            .setMaster("local")
                            .setAppName("sparkOnHiveTest")
                        ).getOrCreate()
    import sparkSession.implicits._
    /**
     * 使用sql操作hive数据
     * 1. load data
     * 2. insert into
     * 3. insert overwrite
     * */
    sparkSession.sql(
      """
        | insert overwrite table taxi
        | select *
        | from taxi
        | where user_id = "A"
        |""".stripMargin
    )
    sparkSession.sql(
      """
        | select
        | *
        | from taxi
        |""".stripMargin
    ).show
  }
}
