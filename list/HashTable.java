package list;

import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;

/**
 * HashTable personalizado para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Colisiones resueltas por encadenamiento usando nodos HashNode<K,V>.
 * No utiliza librerías de Java Collections.
 *
 * @param <K> tipo de clave
 * @param <V> tipo de valor
 */
public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 11;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private HashNode<K, V>[] table;
    private int capacity;
    private int size;

    /**
     * Construye una HashTable con capacidad inicial por defecto.
     */
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (HashNode<K, V>[]) new HashNode[capacity];
        this.size = 0;
    }

    /**
     * Función hash basada en hashCode(), acotada por la capacidad.
     */
    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }

    /**
     * Inserta un par (key,value). Lanza ItemDuplicated si la clave ya existe.
     */
    public void put(K key, V value) throws ItemDuplicated {
        int idx = hash(key);
        HashNode<K, V> node = table[idx];
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {
                throw new ItemDuplicated("Clave duplicada: " + key);
            }
            node = node.getNext();
        }
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.setNext(table[idx]);
        table[idx] = newNode;
        size++;
        if ((double) size / capacity >= DEFAULT_LOAD_FACTOR) {
            rehash();
        }
    }

    /**
     * Obtiene el valor asociado a la clave. Lanza ItemNotFound si no existe.
     */
    public V get(K key) throws ItemNotFound {
        int idx = hash(key);
        HashNode<K, V> node = table[idx];
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
            node = node.getNext();
        }
        throw new ItemNotFound("Clave no encontrada: " + key);
    }

    /**
     * Remueve el par con la clave dada y retorna su valor. Lanza ItemNotFound si no existe.
     */
    public V remove(K key) throws ItemNotFound {
        int idx = hash(key);
        HashNode<K, V> node = table[idx];
        HashNode<K, V> prev = null;
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {
                V val = node.getValue();
                if (prev == null) {
                    table[idx] = node.getNext();
                } else {
                    prev.setNext(node.getNext());
                }
                size--;
                return val;
            }
            prev = node;
            node = node.getNext();
        }
        throw new ItemNotFound("Clave no encontrada: " + key);
    }

    /**
     * Verifica si existe la clave.
     */
    public boolean containsKey(K key) {
        try {
            get(key);
            return true;
        } catch (ItemNotFound e) {
            return false;
        }
    }

    /** @return número de pares almacenados */
    public int size() {
        return size;
    }

    /** @return true si la tabla está vacía */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Limpia la tabla, eliminando todos los pares.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.table = (HashNode<K, V>[]) new HashNode[capacity];
        this.size = 0;
    }

    /**
     * Duplica la capacidad y reubica todos los pares.
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        HashNode<K, V>[] oldTable = table;
        int oldCap = capacity;
        capacity *= 2;
        table = (HashNode<K, V>[]) new HashNode[capacity];
        size = 0;
        for (int i = 0; i < oldCap; i++) {
            HashNode<K, V> node = oldTable[i];
            while (node != null) {
                try {
                    put(node.getKey(), node.getValue());
                } catch (ItemDuplicated ignored) {
                    // no puede ocurrir aquí
                }
                node = node.getNext();
            }
        }
    }
}
