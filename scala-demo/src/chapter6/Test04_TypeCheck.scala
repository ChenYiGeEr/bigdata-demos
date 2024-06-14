package com.lim
package chapter6

import scala.util.Random

/** 类型检查 */
object Test04_TypeCheck {

  def getAnimal():Animal = {
    var integer = Random.nextInt(10);
    if (integer % 2 == 0) {
      new Pig();
    } else {
      new Dog();
    }
  }

  def main(args: Array[String]): Unit = {
    val animal = getAnimal()
    println(animal.getClass)
    println(classOf[Pig])
    println(classOf[Dog])
    println(classOf[Animal])
    if (animal.isInstanceOf[Pig]) {
      var pig = animal.asInstanceOf[Pig];
      println(pig.weight)
    } else {
      var dog = animal.asInstanceOf[Dog];
      println(dog.color)
    }
  }

  class Animal

  class Pig extends Animal {
    var weight: Double = 100.00
  }

  class Dog extends Animal {
    var color: String = "black"
  }
}
