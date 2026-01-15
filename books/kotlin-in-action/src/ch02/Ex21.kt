package ch02.ex21

import ch02.ex17.Expr
import ch02.ex17.Num
import ch02.ex17.Sum


fun evalWithLogging(e: Expr): Int =
    when (e) {
        is Num -> {
            println("Num: ${e.value}")
            e.value
        }
        is Sum -> {
            val left =evalWithLogging(e.left)
            val right =evalWithLogging(e.right)
            println("sum: $left + $right")
            left + right
        }
        else -> throw IllegalArgumentException("Unknown expression")
    }

fun main() {
    println(evalWithLogging(Sum(Sum(Num(1), Num(2)), Num(4)))) // 7
    //Num: 1
    //Num: 2
    //sum: 1 + 2
    //Num: 4
    //sum: 3 + 4
    //7
}