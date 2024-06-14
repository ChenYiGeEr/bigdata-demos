package com.lim

/** 对字符串的练习 */
object Test02_Str {

  /** 主函数 */
  def main(args: Array[String]): Unit = {
    System.out.println()
    // 1. 字符串，通过+号连接
    println("Hello" + " " + "world!")
    // 2. 重复字符串拼接
    println("lim_" * 200)
    // 3. printf用法：字符串，通过%传值。
    printf("name: %s age: %d\n", "linhai", 8)
    // 4. 字符串模板（插值字符串）：通过$获取变量值
    val name = "lim"
    val age = 8

    val s1 = s"name: $name,age:${age}"
    println(s1)

    val s2 = s"name: ${name + 1},age:${age + 2}"
    println(s2)
    // 5. 长字符串  原始字符串
    println("我" +
      "是" +
      "一首" +
      "诗")

    // 多行字符串，在Scala中，利用三个双引号包围多行字符串就可以实现。
    // 输入的内容，带有空格、\t之类，导致每一行的开始位置不能整洁对齐。
    // 应用scala的stripMargin方法，在scala中stripMargin默认是“|”作为连接符，
    // 在多行换行的行头前面加一个“|”符号即可。
    println(
      """我
        |是
        |一首
        |诗
        |""".stripMargin)
    println(
    """
      |select id,
      |       age
      |from  user_info
      |""".stripMargin)

    println(
    s"""
       |${name}
       |${age}
       |""".stripMargin)
  }

}
