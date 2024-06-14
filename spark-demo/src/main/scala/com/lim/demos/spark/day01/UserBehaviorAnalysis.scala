package com.lim.demos.spark.day01

import org.apache.spark.{SparkConf, SparkContext}

import java.text.SimpleDateFormat
import java.util.UUID

// 用户行为分析
object UserBehaviorAnalysis {

  /** 分析用户每个会话[上一次访问和本次访问是否超过半个小时，如果超过则是新会话]的行为轨迹 */
  def main(args: Array[String]): Unit = {
    // 0. 准备工资欧
    val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    // 1. 创建SparkContext对象
    val config:SparkConf = new SparkConf()
//                  .setMaster("local[*]")
                  .setAppName("UserBehaviorAnalysis")
    val sc:SparkContext = new SparkContext(config)
    // 2. 读取数据
    val rdd = sc.textFile(args(0));
    // 3. 转换数据
    rdd.map(element => {
      val arr = element.split(",")
      val userId = arr.head
      val enterTime = arr(1)
      val enterTimeStamp = timeFormat.parse(enterTime).getTime
      val enterHtml = arr(2)
      UserEnterAnalysis(userId, enterTime, enterTimeStamp, enterHtml)
    })
    // 3.1 按照用户编号分组
    .groupBy(_.userId)
    // 3.2 针对用户数据进行排序
    .flatMap(x => {
      // 针对用户的访问时间进行升序排序
      val sortedUserList = x._2.toList.sortBy(_.enterTimeStamp)
      // 滑窗一次滚动2格
      val slidingList = sortedUserList.sliding(2)
      slidingList.foreach(y => {
        // 当前时间戳 - 上次时间戳 <= 30分钟时 符合要求 将前一个的session赋值给后一个，将前一个step+1赋值给后一个
        if (y.last.enterTimeStamp - y.head.enterTimeStamp <=  30 * 60 * 1000) {
          y.last.session = y.head.session
          y.last.step = y.head.step + 1
        }
      })
      x._2
    })
    .collect()
    .foreach(println)
  }

  case class UserEnterAnalysis (userId: String,
                                enterTime: String,
                                enterTimeStamp: Long,
                                enterHtml: String,
                                var session: String = UUID.randomUUID().toString,
                                var step: Int = 1
                                )
}
