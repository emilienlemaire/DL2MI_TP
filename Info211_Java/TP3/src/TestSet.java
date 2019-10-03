public class TestSet {

    public static void main(String[] args) {
        StaticSet s = new StaticSet(10);

        s.add(3);
        s.add(2);
        s.add(1);
        s.add(2);

        System.out.println("Test contains: " + (s.contains(1) && !s.contains(5)));
        System.out.println("Test toString: " + s.toString().equals("1-2-3"));

        DynamicSet dynamicSet = new DynamicSet(3);

        dynamicSet.add(3);
        dynamicSet.add(2);
        dynamicSet.add(1);
        dynamicSet.add(0);


        System.out.println("Test contains: " + (dynamicSet.contains(1) && !dynamicSet.contains(5)));
        System.out.println("Test toString: " + dynamicSet.toString().equals("0-1-2-3"));
    }
}
