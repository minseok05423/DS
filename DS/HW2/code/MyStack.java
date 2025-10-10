public class MyStack<T> extends MyLinkedList<T> implements StackInterface<T>{
    public MyStack() {
        super();
    }

    public void push(T newItem) {
        Node<T> newNode = new Node(newItem);
        head.setNext(newNode);
        newNode.setNext(head.getNext());
    }

    public T pop() {
        T result = null;
        if (size() > 0) {
            result = getNode(0).getItem();
            remove(0);
        }
        return result;
    }

    public T top() {
        return first();
    }

    public void popAll() {
        removeAll();
    }
}
