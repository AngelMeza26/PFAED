package list;

/**
 * Nodo para almacenamiento de pares clave-valor en HashTable.
 * Encadenamiento simple: cada nodo apunta al siguiente en la misma cubeta.
 *
 * @param <K> tipo de clave
 * @param <V> tipo de valor
 */
public class HashNode<K, V> {
    private final K key;
    private V value;
    private HashNode<K, V> next;

    /**
     * Construye un nodo con clave y valor, sin siguiente.
     *
     * @param key   clave del nodo (puede ser null)
     * @param value valor asociado (puede ser null)
     */
    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }

    /** @return la clave de este nodo */
    public K getKey() {
        return key;
    }

    /** @return el valor asociado a esta clave */
    public V getValue() {
        return value;
    }

    /**
     * Asigna un nuevo valor a esta clave.
     *
     * @param value nuevo valor (puede ser null)
     */
    public void setValue(V value) {
        this.value = value;
    }

    /** @return el siguiente nodo en la cadena o null */
    public HashNode<K, V> getNext() {
        return next;
    }

    /**
     * Enlaza este nodo con el siguiente en la misma cubeta.
     *
     * @param next nodo siguiente
     */
    public void setNext(HashNode<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "(" + key + "=" + value + ")";
    }
}
