import java.util.LinkedList;

/* Each node keeps:
- key: the Comparable key used for ordering in the tree
- list: a list of payload values associated with this key (supports duplicate positions)
- left/right: child references
- height: cached subtree height for balance factor calculations */

public class AVLNode<E> {
    public Comparable key;
    public LinkedList<E> list;
    public AVLNode<E> left, right;
    public int height;

    public AVLNode(Comparable newKey) {
        key = newKey;
        list = new LinkedList<>();
        left = right = null;
        height = 1;
    }

    public AVLNode(Comparable newKey, E newItem) {
        key = newKey;
        list = new LinkedList<>();
        list.add(newItem);
        left = right = null;
        height = 1;
    }

    public AVLNode(Comparable newKey, E newItem, AVLNode<E> leftChild, AVLNode<E> rightChild) {
        key = newKey;
        list = new LinkedList<>();
        left = leftChild;
        right = rightChild;
        height = 1 + Math.max(leftChild.height, rightChild.height);
    }
}