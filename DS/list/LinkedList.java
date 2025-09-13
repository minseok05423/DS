package DS.list;

public class LinkedList<E> implements ListInterface<E> {
    private Node<E> head;
    private int numItems;

    public LinkedList() {
        numItems = 0;
        head = new Node(null, null);
    }

    public Node<E> getNode(int k) {
        int i = -1;
        Node<E> currNode = head;

        while (i < k && currNode.next != null) {
            currNode = currNode.next;
            i++;
        }

        if (i == k) {
            return currNode;
        }
        else {
            return null;
        }
    }

    public void add(int index, E item) {
        if (index >= 0 && index <= numItems-1) {
            Node<E> prevNode = getNode(index - 1);
            Node<E> newNode = new Node<>(item, prevNode.next);
            prevNode.next = newNode;
            numItems++;
        }
        else {
            // handle error
        }
    }
    
    public void append(E item) {
        Node<E> lastNode = getNode(numItems - 1);
        Node<E> newNode = new Node<E>(item, null);
        lastNode.next = newNode;
        numItems++;
    }

    public E remove(int index) {
        if (index >= 0 && index < numItems){
            Node<E> prevNode = getNode(index - 1);
            Node<E> currNode = prevNode.next;
            prevNode.next = currNode.next;
            numItems--;
            return currNode.item;}
        else { 
            return null;
        }
    }
    
    public boolean removeItem(E item) {
        Node<E> currNode = head;
        Node<E> prevNode;
        for (int i = 0; i < numItems; i++) {
            prevNode = currNode;
            currNode = currNode.next;
            if (((Comparable)(currNode.item)).compareTo(item) == 0) {
                prevNode.next = currNode.next;
                numItems--;
                return true;
            }
        }
        return false;
    }

    public E get(int index) {
        if (index >= 0 && index < numItems) {
            Node<E> targetNode = getNode(index);
            return targetNode.item;
        }
        else {
            return null;
        }
    }

    public void set(int index, E item) {
        if (index >= 0 && index < numItems) {
            Node<E> targetNode = getNode(index);
            targetNode.item = item;
        } 
        else {
            // error handling
        }

    }

    public final int NOT_FOUND = -12345;
    public int indexOf(E item) {
        Node<E> currNode = head;
        for (int i = 0; i < numItems; i++) {
            currNode = currNode.next;
            if (((Comparable)(currNode.item)).compareTo(item) == 0) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    public int len() {
        return numItems;
    }

    public boolean isEmpty() {
        return numItems == 0;
    }

    public void clear() {
        numItems = 0;
        head = new Node<>(null, null);
    }
}
