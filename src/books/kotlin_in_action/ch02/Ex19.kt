package books.kotlin_in_action.ch02.ex19

import books.kotlin_in_action.ch02.ex17.Expr
import books.kotlin_in_action.ch02.ex17.Num
import books.kotlin_in_action.ch02.ex17.Sum

fun eval(e: Expr): Int =
    if (e is Num) {
        e.value
    } else if (e is Sum) {
        eval(e.left) + eval(e.right)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }

fun main() {
    println(eval(Sum(Num(1), Num(2)))) // 3
}