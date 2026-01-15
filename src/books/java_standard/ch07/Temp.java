package books.java_standard.ch07;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

public class Temp {
    public static class SimpleData {
        int x = 10;
    }

    public static void main(String[] args) {
        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(SimpleData.class).toPrintable());
    }
}
