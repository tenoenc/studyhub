package books.kotlin_in_action.ch02.ex18

import books.kotlin_in_action.ch02.ex17.Expr
import books.kotlin_in_action.ch02.ex17.Num
import books.kotlin_in_action.ch02.ex17.Sum

fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num
        return n.value
    }
    if (e is Sum) {
        return eval(e.left) + eval(e.right)
    }
    throw IllegalArgumentException("Unknown expression")
}

fun main() {
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4)))) // 7
}