package list;

import exceptions.*;
import graph.Vertex;

// LinkedList personalizado para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
// Implementación de TDAList<T> con lista enlazada unidireccional sin usar Iterator.
// Permite acceder a elementos por índice y manipulación dinámica.

public class LinkedList<T> implements TDAList<T> {
    private LinkedNode<T> head;
    private int size;

    // * Construye una lista vacía.
    public LinkedList() {
        head = null;
        size = 0;
    }

    // * Añade un elemento al final de la lista.
    public void add(T data) {
        LinkedNode<T> newNode = new LinkedNode<>(data);
        if (head == null) {
            head = newNode;
        } else {
            LinkedNode<T> curr = head;
            while (curr.getNext() != null) {
                curr = curr.getNext();
            }
            curr.setNext(newNode);
        }
        size++;
    }

    // * Inserta un elemento en la posición indicada.
    public void add(int index, T data) throws ItemDuplicated {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        LinkedNode<T> newNode = new LinkedNode<>(data);
        if (index == 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            LinkedNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.getNext();
            }
            newNode.setNext(curr.getNext());
            curr.setNext(newNode);
        }
        size++;
    }

    // * Limpia la lista, eliminando todos los elementos.
    public void clear() throws IsEmpty {
        if (size == 0) {
            throw new IsEmpty("La lista ya está vacía");
        }
        head = null;
        size = 0;
    }

    // * Devuelve el índice de la primera ocurrencia del elemento, o -1 si no existe.
    public int indexOf(T data) {
        LinkedNode<T> curr = head;
        int idx = 0;
        while (curr != null) {
            if (curr.getData().equals(data)) {
                return idx;
            }
            curr = curr.getNext();
            idx++;
        }
        return -1;
    }

    // * Devuelve el índice de la última ocurrencia del elemento, o -1 si no existe.
    public int lastIndexOf(T data) {
        LinkedNode<T> curr = head;
        int idx = 0, last = -1;
        while (curr != null) {
            if (curr.getData().equals(data)) {
                last = idx;
            }
            curr = curr.getNext();
            idx++;
        }
        return last;
    }

    // * Obtiene el elemento en la posición indicada.
    public T get(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.getNext();
        }
        return curr.getData();
    }

    // * Reemplaza el elemento en la posición indicada.
    public T set(int index, T element) throws ItemNotFound, IsEmpty {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        if (indexOf(element) < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + element);
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.getNext();
        }
        T old = curr.getData();
        curr.setData(element);
        return old;
    }

    // * Elimina el elemento en la posición indicada.
    public T remove(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        if (index == 0) {
            T old = head.getData();
            head = head.getNext();
            size--;
            return old;
        }
        LinkedNode<T> curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.getNext();
        }
        T old = curr.getNext().getData();
        curr.setNext(curr.getNext().getNext());
        size--;
        return old;
    }

    // * Elimina la primera ocurrencia del elemento dado.
    public boolean remove(T data) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        int idx = indexOf(data);
        if (idx < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + data);
        }
        remove(idx);
        return true;
    }

    // * Devuelve el número de elementos en la lista.
    public int size() {
        return size;
    }

    // * Verifica si la lista está vacía.
    public boolean isEmpty() {
        return size == 0;
    }

    // * Comprueba si la lista contiene el elemento dado.
    public boolean contains(T data) {
        return indexOf(data) >= 0;
    }

    // * Representación en cadena de los elementos.
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        LinkedNode<T> curr = head;
        while (curr != null) {
            sb.append(curr.getData());
            if (curr.getNext() != null) sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    // MÉTODOS ADICIONALES

    // * Elimina y devuelve el primer elemento de la lista.
    public T removeFirst() throws IsEmpty, ItemNotFound {
        return remove(0);
    }

    // * Añade un elemento al final de la lista (alias de add()).
    public void addLast(T data) {
        add(data);
    }
}
