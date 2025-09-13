import DS.list.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();

        list.append(10);
        list.append(20);
        list.add(1, 15);

        System.out.println("Size: " + list.len());
        System.out.println("First element: " + list.get(0));
        System.out.println("Index of 15: " + list.indexOf(15));
    }
}