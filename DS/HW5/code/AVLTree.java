import java.util.LinkedList;

/* Generic AVL tree implementation providing logarithmic-time insert/search/delete.
Keys are stored inside {@link AVLNode} instances; each key can map to multiple payload
entries (kept in the node's list) which is useful for indexing duplicate positions in text. */

public class AVLTree<E> implements IndexInterface<E, AVLNode<E>>{
    private AVLNode<E> root;
    private final AVLNode<E> NIL;

    public AVLTree() {
        NIL = new AVLNode<>(null);
        NIL.height = 0;
        root = NIL;
    }   

    public AVLNode<E> search(Comparable key) {
        return searchKey(root, key);
    }   

    // Recursive binary search down from node t
    private AVLNode<E> searchKey(AVLNode<E> t, Comparable key) {
        if (t == NIL)
            return null;

        int cmp = key.compareTo(t.key);
        if (cmp == 0)
            return t;
        else if (cmp < 0)
            return searchKey(t.left, key);
        else
            return searchKey(t.right, key);
    }

    
    /* Insert (key,payload). If key already exists, payload is appended to its list, enabling
    aggregation of multiple occurrences. Otherwise a new node is created and AVL balance restored */
    
    public void insert(Comparable key, E payload) {
        root = insertItem(root, key, payload);
    }

    /* Recursive insert that returns the (possibly new / rotated) subtree root */
    private AVLNode<E> insertItem(AVLNode<E> t, Comparable key, E payload) {
        if (t == NIL) {
            AVLNode<E> newNode = new AVLNode<>(key, payload);
            newNode.left = NIL;
            newNode.right = NIL;
            return newNode;
        }

        int cmp = key.compareTo(t.key);
        if (cmp == 0) {
            t.list.add(payload);
        }
        else if (cmp < 0) {
            t.left = insertItem(t.left, key, payload);
            t.height = 1 + Math.max(t.left.height, t.right.height);
            int type = needBalance(t);
            if (type != 0) {
                t = balance(t, type);
            }
        }
        else if (cmp > 0) {
            t.right = insertItem(t.right, key, payload);
            t.height = 1 + Math.max(t.right.height, t.left.height);
            int type = needBalance(t);
            if (type != 0) {
                t = balance(t, type);
            }
        }

        return t;
    }

    // Deletion is retained for completeness
    public void delete(Comparable key) {
        root = deleteItem(root, key);
    }

    private AVLNode<E> deleteItem(AVLNode<E> t, Comparable key) {
        if (t == NIL)
            return NIL;

        int cmp = key.compareTo(t.key);
        if (cmp == 0) {
            t = deleteNode(t);
        }
        else if (cmp < 0) {
            t.left = deleteItem(t.left, key);
            t.height = 1 + Math.max(t.left.height, t.right.height);
            int type = needBalance(t);
            if (type != 0) {
                t = balance(t, type);
            }
        }
        else {
            t.right = deleteItem(t.right, key);
            t.height = 1 + Math.max(t.right.height, t.left.height);
            int type = needBalance(t);
            if (type != 0) {
                t = balance(t, type);
            }
        }

        return t;
    }

    private AVLNode<E> deleteNode(AVLNode<E> t) {
        if (t.left == NIL && t.right == NIL) {
            return NIL;
        }
        else if (t.left == NIL) {
            return t.right;
        }
        else if (t.right == NIL) {
            return t.left;
        }
        else {
            AVLNode<E> successor = getMin(t.right);
            t.list = successor.list;
            t.right = deleteItem(t.right, successor.key);
            t.height = 1 + Math.max(t.left.height, t.right.height);
            int type = needBalance(t);
            if (type != 0) {
                t = balance(t, type);
            }
            return t;
        }
    }

    private AVLNode<E> getMin(AVLNode<E> t) {
        if (t.left == NIL) {
            return t;
        }
        else {
            return getMin(t.left);
        }
    }

    // Apply the indicated rotation type to restore AVL balance and return new subtree root
    private AVLNode<E> balance(AVLNode<E> t, int type) {
        AVLNode<E> returnNode = NIL;
        switch (type) {
            case 1:
                // LL
                returnNode = rightRotate(t);
                break;
            case 2:
                // RR
                returnNode = leftRotate(t);
                break;
            case 3:
                // LR
                t.left = leftRotate(t.left);
                returnNode = rightRotate(t);
                break;
            case 4:
                // RL
                t.right = rightRotate(t.right);
                returnNode = leftRotate(t);
                break;
        }

        return returnNode;
    }

    private int needBalance(AVLNode<E> t) {
        if (t == NIL)
            return 0;
        int balanceFactor = t.left.height - t.right.height;
        if (balanceFactor > 1) {
            // Left heavy
            if (t.left.left.height >= t.left.right.height) {
                // LL
                return 1;
            }
            else {
                // LR
                return 3;
            }
        }
        else if (balanceFactor < -1) {
            // Right heavy
            if (t.right.right.height >= t.right.left.height) {
                // RR
                return 2;
            }
            else {
                // RL
                return 4;
            }
        }

        return 0; // No need to balance
    }

    // Left rotation around node t (t becomes left child of its right child)
    private AVLNode<E> leftRotate(AVLNode<E> t) {
        AVLNode<E> RChild = t.right;
        if (RChild == NIL) {
            System.err.println(t.key + "'s RChild shouldn't be NIL");
        }
        AVLNode<E> RLChild = RChild.left;
        RChild.left = t;
        t.right = RLChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);

        return RChild;
    }

    // Right rotation around node t (t becomes right child of its left child)
    private AVLNode<E> rightRotate(AVLNode<E> t) {
        AVLNode<E> LChild = t.left;
        if (LChild == NIL) {
            System.err.println(t.key + "'s LChild shouldn't be NIL");
        }
        AVLNode<E> LRChild = LChild.right;
        LChild.right = t;
        t.left = LRChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);

        return LChild;
    }

    // Pre-order traversal starting from root
    public LinkedList<String> rootTraverse(LinkedList<String> result) {
        preOrder(root, result);
        return result;
    }

    private LinkedList<String> preOrder(AVLNode<E> r, LinkedList<String> result) {
        if (r != NIL) {
            result.offer(r.key.toString());
            preOrder(r.left, result);
            preOrder(r.right, result);
        }
        return result;
    }

    public boolean isEmpty() {
        return root == NIL;
    }

    public void clear() {
        root = NIL;
    }
}
