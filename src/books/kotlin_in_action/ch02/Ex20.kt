package books.kotlin_in_action.ch02.ex20

import books.kotlin_in_action.ch02.ex17.Expr
import books.kotlin_in_action.ch02.ex17.Num
import books.kotlin_in_action.ch02.ex17.Sum

fun eval(e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.left) + eval(e.right)
        else -> throw IllegalArgumentException("Unknown expression")
    }