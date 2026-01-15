package ch07;

class Outer2 {
    int value = 10;

    class Inner {
        int value = 20;

        void method1() {
            int value = 30;
            System.out.println("value: " + value);
            System.out.println("this.value: " + this.value);
            System.out.println("Outer2.this.value: " + Outer2.this.value);
        }
    }
}

public class InnerEx5 {
    public static void main(String[] args) {
        Outer2 outer2 = new Outer2();
        Outer2.Inner inner = outer2.new Inner();
        inner.method1();
    }
}
