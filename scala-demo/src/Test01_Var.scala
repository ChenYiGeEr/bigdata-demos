package com.lim

/** 类型练习 */
object Test01_Var {

  /** 主函数 */
  def main(args: Array[String]): Unit = {
    // 声明常量和变量
    val a: Int = 10;
    var b: Int = 10;
    // 1. 常量和变量的区别，常量无法修改
    //    a = 20;
    b = 15;
    // 2. 声明常量时，可以不指定类型，编译器会自动推断
    val c = 20;
    var d = 10;
    // 3. 类型确定后，就不能修改，说明Scala是强数据类型语言
    //    d = "20";
    // 4. 变量声明时，必须要有初始值
    // var a: Int;
    // 5. var修饰的对象引用可以改变，val修饰的对象则不可改变，但对象的状态(值)却是可以改变的
    val person1: Person = new Person();
    var person2: Person = new Person();
    // 引用数据类型的常量和变量能否替换成别的对象
    // var 可以修改引用数据类型的地址值  val不行
    //    person1 = new Person();
    person2 = new Person();
    // 6. 引用数据类型中的属性值能否发生变化  取决于内部的属性在定义的时候是var还是val
    //    person2.name = "lim";
    person2.age = 18;
  }

}
