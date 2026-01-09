package books.kotlin_in_action.ch02.ex14

import books.kotlin_in_action.ch02.ex11.Color
import books.kotlin_in_action.ch02.ex11.Color.*

fun getWarmth(color: Color) = when(color) {
    RED, ORANGE, YELLOW -> "warm"
    GREEN -> "neutral"
    BLUE, INDIGO, VIOLET -> "cold"
}