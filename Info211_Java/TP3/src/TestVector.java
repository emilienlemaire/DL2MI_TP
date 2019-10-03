public class TestVector {

    public static void main(String[] args) {
        Vector v1 = new Vector(1,2,3);
        Vector v2 = new Vector(3,4,5);
        Vector v3 = v1.add(v2);
        Vector v4 = new Vector();

        System.out.println(v3.toString());
        System.out.println(v1.dot(v2));
        System.out.println(v1.norm());
        System.out.println(v4.toString());
    }

}
