package ch02;

import ch02.ex05.Person;

public class Ex06 {
    public static void main(String[] args) {
        Person person = new Person("Bob", true);
        System.out.println(person.getName()); // Bob
        System.out.println(person.isMarried()); // true
    }
}