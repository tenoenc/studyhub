package ch07;

public class DefaultMethodTest {
    public static void main(String[] args) {
        Child6 c = new Child6();
        c.method1();
        c.method2();
        MyInterface6.staticMethod();
        MyInterface7.staticMethod();
    }
}

class Child6 extends Parent6 implements MyInterface6, MyInterface7 {
    public void method1() {
        System.out.println("method1() in Child");
    }
}

class Parent6 {
    public void method2() {
        System.out.println("method2() in Parent");
    }
}

interface MyInterface6 {
    default void method1() {
        System.out.println("method1() in MyInterface");
    }
    default void method2() {
        System.out.println("method2() in MyInterface");
    }
    static void staticMethod() {
        System.out.println("staticMethod() in MyInterface");
    }
}

interface MyInterface7 {
    default void method1() {
        System.out.println("method1() in MyInterface2");
    }
    static void staticMethod() {
        System.out.println("staticMethod() in MyInterface2");
    }
}

