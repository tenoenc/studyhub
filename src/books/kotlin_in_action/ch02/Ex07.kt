package books.kotlin_in_action.ch02.ex07

import books.kotlin_in_action.ch02.ex05.Person

fun main() {
    val person = Person("Bob", true)
    println(person.name) // Bob
    println(person.isMarried) // true
}