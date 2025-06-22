package list;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;

// Interfaz TDAList para el TDA ArrayList y LinkedList.
// Define la estructura abstracta de una lista indexada dinámica.

public interface TDAList<E> {
    // * Añade un elemento al final de la lista.
    void add(E e) throws ItemDuplicated;

    // * Inserta un elemento en la posición indicada.
    void add(int index, E e) throws ItemDuplicated, ItemNotFound;

    // * Limpia la lista, eliminando todos los elementos.
    void clear() throws IsEmpty;

    // * Devuelve el índice de la primera ocurrencia del elemento, o -1 si no existe.
    int indexOf(E e);

    // * Devuelve el índice de la última ocurrencia del elemento, o -1 si no existe.
    int lastIndexOf(E e);

    // * Obtiene el elemento en la posición indicada.
    E get(int index) throws IsEmpty, ItemNotFound;

    // * Reemplaza el elemento en la posición indicada.
    E set(int index, E element) throws ItemNotFound, IsEmpty;

    // * Elimina el elemento en la posición indicada.
    E remove(int index) throws IsEmpty, ItemNotFound;

    // * Elimina la primera ocurrencia del elemento dado.
    boolean remove(E e) throws IsEmpty, ItemNotFound;

    // * Devuelve el número de elementos en la lista.
    int size();

    // * Verifica si la lista está vacía.
    boolean isEmpty();

    // * Representación en cadena de los elementos.

    @Override
    String toString();
}
