public interface StackInterface<T> {
    public void push(T newItem);
    public T pop();
    public T top();
    public boolean isEmpty();
    public void popAll();
}
