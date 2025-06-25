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
    private static final double DEFAULT_LOAD_FACTOR = 0.75;//capacidad de carga de %75

    private HashNode<K, V>[] table;//cada posicion es la posicion es la cabeza de lislaa
    private int capacity;
    private int size;

    //Construimos una HashTable con capacidad inicial por defecto.
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (HashNode<K, V>[]) new HashNode[capacity];
        this.size = 0;//estara vacia!
    }

    // Función HASH basada en hashCode(), acotada por la capacidad (11)
    
    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode())) % capacity;
    }

    // Inserta un par (key,value). Lanza ItemDuplicated si la clave ya existe.
     
    public void put(K key, V value) throws ItemDuplicated {
        int idx = hash(key);//OBTENEMOS UN ENTERO DONNDE IDX SEÑALARA EN QUE posicipon se almacenara  el key y el value
        HashNode<K, V> node = table[idx];//empieza al inicio de la lista 
        while (node != null) {
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {//verificamos que no voten null
                throw new ItemDuplicated("Clave duplicada: " + key);//si conincide llama itemduplicate, diciendo que ya existe en la tbla
            }
            node = node.getNext();
        }
        HashNode<K, V> newNode = new HashNode<>(key, value);//creamos un nodohash
        newNode.setNext(table[idx]);//al inicio esta el hash node
        table[idx] = newNode;//actualiza, el noco nuevo estara en la cabeza
        size++;
        if ((double) size / capacity >= DEFAULT_LOAD_FACTOR) {//SI SUPERA EL %75 REHASHEA
            rehash();
        }
    }

    // Obtiene el valor asociado a la clave. Lanza ItemNotFound si no existe.
    //BUSCAREMOS POR MEDIO DE LA LCAVE
    public V get(K key) throws ItemNotFound {
        int idx = hash(key);
        HashNode<K, V> node = table[idx];//CABEZA DE LA LISTA ENLAZADA
        while (node != null) {
            //si ambas claves son null son iguales            si qui no es null llama a ...
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
            node = node.getNext();//pasa al siguiente nodo
        }
        throw new ItemNotFound("Clave no encontrada: " + key);
    }

    // Remueve el par con la clave dada y retorna su valor. Lanza ItemNotFound si no existe.
    
    public V remove(K key) throws ItemNotFound {
        int idx = hash(key);
        HashNode<K, V> node = table[idx];//NODE VA A RECORRER LA LLISLA ENLAZADA 
        HashNode<K, V> prev = null;//prev queda apuntando al nodo anterior a node (inicialmente null).
        while (node != null) {
            //SI LO QUE BUSCO ES igual al que esta almacenado es null son iguales
            //verificmos que nuestra key no sea null para no provacar key.equals(node.getKey())
            if ((key == null && node.getKey() == null) || (key != null && key.equals(node.getKey()))) {
                V val = node.getValue(); //antes de extraerlo de HT extraemos su valor
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

    // Verifica si existe la clave.

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

    // Duplica la capacidad y reubica todos los pares.
    //rehasheo AL %75
    
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
