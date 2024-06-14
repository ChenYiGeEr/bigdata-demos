package com.lim
package test

import scala.io.Source
import scala.util.Try

object Test01 {

  /**
   * 1. 获取没有农贸农场的省份
   * 2. 统计菜的种类数最多的三个省份
   * 3. 统计每个省份菜的种类数最多的三个农贸市场
   * */
  def main(args: Array[String]): Unit = {
    // 1.读取数据
    var provinces = Source.fromFile("datas/allprovince.txt").getLines().toList
    var products = Source.fromFile("datas/product.txt").getLines().toList
    // 2. 统计没有农贸农场的省份
    getNoMarketProvince(provinces, products).foreach(println)
    println("________________________________________________________________")
    // 3. 统计菜的种类数最多的三个省份
    getTopNCategoriesProvince(products, 3).foreach(println)
    println("________________________________________________________________")
    // 4. 统计每个省份菜的种类数最多的三个农贸市场
    getTopNProvinceMarkets(products, 3).foreach(println)
    println("________________________________________________________________")
  }

  /** 统计没有农贸农场的省份 */
  def getNoMarketProvince(provinces: List[String], products: List[String]): List[String] = {
    // 2.1 获取有农贸市场的省份
    var provincesWithMarkets =
      products
        .map(element => {
          Try(element.split("\t")(4)).getOrElse("")
        })
        .distinct
        .filter(_.nonEmpty)
    // 2.2 获取没有农贸市场的省份 = provinces.diff(provincesWithMarkets)
    provinces.diff(provincesWithMarkets)
  }

  /** 统计菜的种类数最多的三个省份 */
  def getTopNCategoriesProvince(products: List[String], n: Int): List[(String, Int)] = {
    products
      // 1 过滤出没有省份或没有菜名的数据
      .filter(element => {
        Try({
          val splits = element.split("\t")
          splits(0).nonEmpty && splits(4).nonEmpty
        }).getOrElse(false)
      })
      .map(element => {
        val splits = element.split("\t")
        (Try(element.split("\t")(4)).getOrElse(""), splits(0))
      })
      .distinct
      .groupBy(_._1)
      .map(element => {
        (element._1, element._2.size)
      })
      .toList
      .sortBy(_._2)
      .reverse
      .take(n)
  }

  /** 统计每个省份菜的种类数最多的三个农贸市场 */
  // List[((String, String), Int)]
  def getTopNProvinceMarkets(products: List[String], n: Int):List[(String, List[(String, Int)])] = {
    // 省份，农贸市场名称，菜品类个数
    products
      // 1 过滤出有省份且有农贸市场名称且有菜种类的数据
      .filter(element => {
        Try({
          val splits = element.split("\t")
          splits(0).nonEmpty && splits(3).nonEmpty && splits(4).nonEmpty
        }).getOrElse(false)
      })
      .map(element => {
        val splits = element.split("\t")
        ((Try(element.split("\t")(4)).getOrElse("") , Try(element.split("\t")(3)).getOrElse("")), splits(0))
      })
      .distinct
      .groupBy(_._1)
      .map(element => {
        ((element._1._1,element._1._2), element._2.size)
      })
      .groupBy(_._1._1)
      .map(x => {
        val topN = x._2.toList.sortBy(_._2).reverse.take(n)
        (x._1, topN.map(element => {
          (element._1._2, element._2)
        }))
      }).toList
  }
}
