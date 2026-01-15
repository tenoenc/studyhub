package books.java_standard.ch07;

public class CastingTest2 {
    public static void main(String[] args) {
//        Car2 car = new Car2();
        Car2 car = new FireEngine2();
        Car2 car2 = null;
        FireEngine2 fe = null;

        car.drive();
        fe = (FireEngine2) car;
        fe.drive();
        car2 = fe;
        car2.drive();
    }
}

class Car2 {
    String color;
    int door;

    void drive() {
        System.out.println("drive, Brrrr~");
    }

    void stop() {
        System.out.println("stop!!!");
    }
}

class FireEngine2 extends Car2 {
    void water() {
        System.out.println("water!!!");
    }
}