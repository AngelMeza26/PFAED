package list;

/**
 * Nodo para lista enlazada genérica.
 * Clase independiente de LinkedList para contener dato y referencia al siguiente nodo.
 * @param <T> tipo de dato almacenado
 */
public class LinkedNode<T> {
    private T data;
    private LinkedNode<T> next;

    /**
     * Crea un nodo con el dato especificado.
     * @param data valor del nodo
     */
    public LinkedNode(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Obtiene el dato del nodo.
     * @return dato almacenado
     */
    public T getData() {
        return data;
    }

    /**
     * Establece el dato del nodo.
     * @param data nuevo valor del nodo
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene la referencia al siguiente nodo.
     * @return siguiente nodo
     */
    public LinkedNode<T> getNext() {
        return next;
    }

    /**
     * Establece la referencia al siguiente nodo.
     * @param next nodo que sigue
     */
    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "null";
    }
}
