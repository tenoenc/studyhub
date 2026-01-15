package ch02.ex07

import ch02.ex05.Person

fun main() {
    val person = Person("Bob", true)
    println(person.name) // Bob
    println(person.isMarried) // true
}