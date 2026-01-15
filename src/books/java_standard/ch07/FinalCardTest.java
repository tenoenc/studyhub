package books.java_standard.ch07;

class Card3 {
    final int NUMBER;
    final String KIND;
    static int width = 100;
    static int height = 250;
    Card3(String kind, int num) {
        NUMBER = num;
        KIND = kind;
    }

    Card3() {
        this("HEART", 1);
    }

    public String toString() {
        return KIND + " " + NUMBER;
    }

}

public class FinalCardTest {
    public static void main(String[] args) {
        Card3 c = new Card3("HEART", 10);
//        c.NUMBER = 5;
        System.out.println(c.KIND);
        System.out.println(c.NUMBER);
        System.out.println(c);
    }
}
