package ch07;

public class PointTest {
    public static void main(String[] args) {
        Point3D2 p3 = new Point3D2(1, 2, 3);
    }
}

class Point2 {
    int x, y;

    Point2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    String getLocation() {
        return "x: " + x + ", y : " + y;
    }
}

class Point3D2 extends Point2 {
    int z;

    Point3D2(int x, int y, int z) {
        super(x, y); // 이게 없으면 super()가 삽입되고, 부모 기본 생성자가 없으므로 컴파일 에러가 발생한다.
        this.x = x;
        this.y = y;
        this.z = z;
    }

    String getLocation() {
        return "x: " + x + ", y:" + y + "z, z:" + z;
    }
}
