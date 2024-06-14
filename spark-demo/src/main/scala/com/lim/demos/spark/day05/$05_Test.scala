package com.lim.demos.spark.day05

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

import scala.collection.mutable

/**
 * 用户行为数据
 * date	String	用户点击行为的日期
 * user_id	Long	用户的ID
 * session_id	String	Session的ID
 * page_id	Long	某个页面的ID
 * action_time	String	动作的时间点
 * search_keyword	String	用户搜索的关键词
 * click_category_id	Long	点击某一个商品品类的ID
 * click_product_id	Long	某一个商品的ID
 * order_category_ids	String	一次订单中所有品类的ID集合
 * order_product_ids	String	一次订单中所有商品的ID集合
 * pay_category_ids	String	一次支付中所有品类的ID集合
 * pay_product_ids	String	一次支付中所有商品的ID集合
 * city_id	Long	城市 id
 * */
class $05_Test {

  val sc = new SparkContext(
    new SparkConf()
      .setMaster("local")
      .setAppName("broadcastTest")
  )
  // 开启checkpoint
  sc.setCheckpointDir("datas/checkpoint")
  val rdd: RDD[String] = sc.textFile("datas/user_visit_action.txt")
  rdd.persist(StorageLevel.MEMORY_AND_DISK)

  /**
   * 需求：按照每个品类的点击、下单、支付的量（次数）来统计热门品类
   * 鞋			点击数 下单数  支付数
   * 衣服		点击数 下单数  支付数
   * 电脑		点击数 下单数  支付数
   * 综合排名 = 点击数*20% + 下单数*30% + 支付数*50%
   * 优化需求
   * 先按照点击数排名，靠前的就排名高；
   * 如果点击数相同，再比较下单数；
   * 下单数再相同，就比较支付数。
   * */
  @Test
  def test01(): Unit = {
    // 列裁剪
    val rdd1 = rdd.map(line => {
      val splits = line.split("_")
      // 搜索词
      val searchKeyword = splits(5)
      // 点击编号
      val clickId = splits(6)
      // 下单编号
      val orderIds = splits(8)
      // 支付编号
      val payIds = splits(10)
      (searchKeyword, clickId, orderIds, payIds)
    })
    // 过滤掉搜索行为数据
    val rdd2 = rdd1.filter(_._1 == "null")
    // 缓存rdd2
    rdd2.cache
    rdd2.checkpoint()
    // 统计每个品类的点击数
    val clickCategoryAndTimes =
      rdd2
        .filter(_._2 != "-1")
        .map({
          case (searchKeyword, clickId, orderIds, payIds) => (clickId, 1)
        })
        .reduceByKey(_ + _)
    // 统计每个品类的下单数
    val orderCategoryAndTimes =
      rdd2
        .filter(_._3 != "null")
        .flatMap({
          case (searchKeyword, clickId, orderIds, payIds) => {
            val categoryIds = orderIds.split(",")
            categoryIds.map((_, 1))
          }
        })
        .reduceByKey(_ + _)
    // 统计每个品类的支付数
    val payCategoryAndTimes =
      rdd2
        .filter(_._4 != "null")
        .flatMap({
          case (searchKeyword, clickId, orderIds, payIds) => {
            val categoryIds = payIds.split(",")
            categoryIds.map((_, 1))
          }
        })
        .reduceByKey(_ + _)
    // 三者join 按照点击数、下单数、支付数降序排序取前十
    val clickOrderRdd = clickCategoryAndTimes.fullOuterJoin(orderCategoryAndTimes)
    val clickOrderTimeRdd = clickOrderRdd.map({
      case (id, (clickTimes, orderTimes)) => (id, (clickTimes.getOrElse(0), orderTimes.getOrElse(0)))
    })
    val totalRdd = clickOrderTimeRdd.fullOuterJoin(payCategoryAndTimes)
    val totalTimesRdd = totalRdd.map({
      case (id, (clickOrderTimes, payTimes)) => {
        val tuple = clickOrderTimes.getOrElse((0, 0))
        (id, tuple._1, tuple._2, payTimes.getOrElse(0))
      }
    })
    val result: Array[(String, Int, Int, Int)] = totalTimesRdd.sortBy({
      case (id, clickTimes, orderTimes, payTimes) => (clickTimes, orderTimes, payTimes)
    }, false).take(10)
    // 结果打印
    result.foreach(println)
    // Thread.sleep(1000000);
  }

  @Test
  def test02(): Unit = {
    // 1. 列裁剪
    val rdd1 = rdd.map(line => {
      val splits = line.split("_")
      // 搜索词
      val searchKeyword = splits(5)
      // 点击商品分类编号
      val clickCategoryId = splits(6)
      // 下单商品分类编号
      val orderCategoryIds = splits(8)
      // 支付商品分类编号
      val payCategoryIds = splits(10)
      (searchKeyword, clickCategoryId, orderCategoryIds, payCategoryIds)
    })
    // 2. 过滤掉搜索行为数据
    val rdd2 = rdd1.filter(_._1 == "null")
    // 缓存rdd2
    rdd2.cache
    rdd2.checkpoint()
    // 3. 炸开 clickCategoryId payCategoryIds
    val rdd3: RDD[(String, (Int, Int, Int))] = rdd2.flatMap({
      case (searchKeyword, clickCategoryId, orderCategoryIds, payCategoryIds) => {
        if (clickCategoryId != "-1") {
          (clickCategoryId, (1, 0, 0)) :: Nil
        } else if (orderCategoryIds != "null") {
          orderCategoryIds.split(",").map((_, (0, 1, 0)))
        } else {
          payCategoryIds.split(",").map((_, (0, 0, 1)))
        }
      }
    })
    // 4. 聚合
    // val rdd4:RDD[(String, (Int, Int, Int))] = rdd3.reduceByKey((agg, curr) => (agg._1 + curr._1, agg._2 + curr._2, agg._3 + curr._3))
    // 4. 使用累加器替换reduceByKey
    val categoryAccumulator = sc.collectionAccumulator[mutable.Map[String, (Int, Int, Int)]]("categoryAccumulator")
    rdd3.foreachPartition(iter => {
      val map = mutable.Map[String, (Int, Int, Int)]()
      iter.foreach(t => {
        val key = t._1
        val times = map.getOrElse(key, (0, 0, 0))
        val clickTimes = t._2._1
        val orderTimes = t._2._2
        val payTimes = t._2._3
        map.put(key, (times._1 + clickTimes, times._2 + orderTimes, times._3 + payTimes))
      })
      categoryAccumulator.add(map)
    })
    // 4.1 隐式转换 将java集合转换为scala集合
    import scala.collection.JavaConverters._
    // 5. 排序取前十
    val resultRdd = categoryAccumulator.value
      .asScala
      .flatten
      .groupBy(_._1)
      .map(x => {
        val time = x._2
        val clickTimes = time.map(item => {
          item._2._1
        })
        val orderTimes = time.map(item => {
          item._2._2
        })
        val payTimes = time.map(item => {
          item._2._3
        })
        (x._1, (clickTimes.sum, orderTimes.sum, payTimes.sum))
      })
      .toList
      .sortBy({
        case (id, (clickTimes, orderTimes, payTimes)) => (clickTimes, orderTimes, payTimes)
      }).reverse.take(10)
    // 6. 结果打印
    resultRdd.foreach(println)
    Thread.sleep(1000000);
  }
}
