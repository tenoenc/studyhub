package books.java_standard.ch07;

import kotlin.Unit;

public class FighterTest {
    public static void main(String[] args) {
        Fighter2 f = new Fighter2();

        if (f instanceof Unit2) {
            System.out.println("f는 Unit 클래스의 자손입니다.");
        }

        if (f instanceof Fightable2) {
            System.out.println("f는 Fightable 인터페이스를 구현했습니다.");
        }

        if (f instanceof Movable2) {
            System.out.println("f는 Movable2 인터페이스를 구현했습니다.");
        }

        if (f instanceof Attackable2) {
            System.out.println("f는 Attackable2 인터페이스를 구현했습니다.");
        }

        if (f instanceof Object) {
            System.out.println("f는 Object 클래스의 자손입니다.");
        }
    }
}

class Fighter2 extends Unit2 implements Fightable2 {
    public void move(int x, int y) {

    }

    public void attack(Unit2 u) {

    }
}

class Unit2 {
    int currentHP;
    int x;
    int y;
}

interface Fightable2 extends Movable2, Attackable2 {}
interface Movable2 { void move(int x, int y); }
interface Attackable2 { void attack(Unit2 u); }