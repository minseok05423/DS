import java.util.LinkedList;

public interface IndexInterface<E, T> {
    void insert(Comparable key, E payload);
    void delete(Comparable key);
    T search(Comparable key);
    LinkedList<String> rootTraverse(LinkedList<String> result);
    boolean isEmpty();
    void clear();
}