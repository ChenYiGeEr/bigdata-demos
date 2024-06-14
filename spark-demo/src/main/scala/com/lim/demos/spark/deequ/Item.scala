package com.lim.demos.spark.deequ

case class Item(
  /** 主键编号 唯一非空 */
  id: Long,
  /** 产品名称 唯一非空 */
  produceName: String,
  /** 产品描述 需包含一个url */
  description: String,
  /** 邮箱 */
  email: String,
  /** 优先级 只能是high或low */
  priority: String,
  /** 查看次数 只能是不为负数的数字且<=10 */
  numViews: Long
)