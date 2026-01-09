package books.kotlin_in_action.ch02.ex15

import books.kotlin_in_action.ch02.ex11.Color
import books.kotlin_in_action.ch02.ex11.Color.*

fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)) {
        setOf(RED, YELLOW) -> ORANGE
        setOf(YELLOW, BLUE) -> GREEN
        setOf(BLUE, VIOLET) -> INDIGO
        else -> throw Exception("Dirty color")
    }

fun main() {
    println(mix(BLUE, YELLOW)) // GREEN
}