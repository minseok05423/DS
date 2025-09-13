package DS.list;

public class ArrayList<E> implements ListInterface<E> {
    private E item[];
    private int numItems;
    private static final int DEFAULT_CAPACITY = 64;

    public ArrayList() {
        item = (E[]) new Object[DEFAULT_CAPACITY];
        numItems = 0;
    }

    public ArrayList(int n) {
        item = (E[]) new Object[n];
        numItems = 0;
    }

    public void add(int index, E x) {
        if (numItems >= item || index < 0 || index > numItems) {
            
        }
    }
}
