package com.lim.demos.spark.day05

import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

/**
 * 广播变量：分布式共享只读变量
 *    用来高效分发较大的对象。向所有工作节点发送一个较大的只读值，以供一个或多个Spark Task操作使用
 *  */
class $04_Broadcast {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local[2]")
      .setAppName("broadcastTest")
  )

  /** 广播变量测试 */
  @Test
  def testBroadcast(): Unit = {
    val rdd = sc.parallelize(List("atguigu", "jd", "pdd", "tm"))
    val map = Map("atguigu" -> "https://www.atguigu.com", "jd" -> "https://www.jd.com", "pdd" -> "https://www.pdd.com", "tm" -> "https://www.tm.com")
    val mapBroadcast = sc.broadcast(map)
    rdd.map(x => {
      val url = mapBroadcast.value.getOrElse(x, s"https://www.baidu.com?wd=${x}")
      (x, url)
    })
    .foreach(println)
  }

}
