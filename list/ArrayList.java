package list;

import exceptions.*;

// ArrayList personalizado genérico para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
// Implementa almacenamiento dinámico y métodos auxiliares de búsqueda y manipulación sin usar Iterator.
// Permite recorrer elementos con bucles indexados convencional.

public class ArrayList<T> implements TDAList<T> {
	private Object[] elements; //CA SE UARDAN TDOS LOS ELEMENTOS
    private int size = 0; // ES EL TAMAÑO DE LA LISTA 
    private static final int DEFAULT_CAPACITY = 10;// LA CAPACIDAD POR DEFECTO

    // * Construye una lista con capacidad inicial por defecto.
    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY]; // CONSTRUYE LA LISTA
    }

    // * Añade un elemento al final de la lista.
    public void add(T e) throws ItemDuplicated {//AÑADE UN ELEMENTO AL FINAL DE LA LISTAA
        // ** Comprueba duplicado antes de agregar
        if (size > 0 && indexOf(e) >= 0) {
            throw new ItemDuplicated("Elemento ya existe: " + e);
        }
        ensureCapacity();
        elements[size++] = e;
    }

    // * Inserta un elemento en la posición indicada.
    public void add(int index, T e) throws ItemDuplicated, ItemNotFound {
        // Verifica índice válido
        if (index < 0 || index > size) {// VERIFICA QUE LOS INDICES ESTAN EN LOS LIMERST
            throw new ItemNotFound("Índice inválido: " + index);
        }
        // Duplicado solo si lista no vacía
        if (size > 0 && indexOf(e) >= 0) {
            throw new ItemDuplicated("Elemento ya existe: " + e);
        }
        ensureCapacity();
        // Desplazar
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = e;
        size++;
    }

    // * Limpia la lista, eliminando todos los elementos.
    public void clear() throws IsEmpty { // ELIMINA TODA LA LSITA 
        if (size == 0) {
            throw new IsEmpty("La lista ya está vacía");
        }
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // * Devuelve el índice de la primera ocurrencia del elemento, o -1 si no existe.
    public int indexOf(T e) { //MUESTRA LA PRIMERA OCURRENCIA DLE ELEENTO E
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    // * Devuelve el índice de la última ocurrencia del elemento, o -1 si no existe.
    public int lastIndexOf(T e) {// MUETSRA LA ULTIMA OCURRENCIA DE ELEMENTO
        for (int i = size - 1; i >= 0; i--) {
            if (elements[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    // * Obtiene el elemento en la posición indicada.
    @SuppressWarnings("unchecked")
    public T get(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        return (T) elements[index];
    }

    // * Reemplaza el elemento en la posición indicada.
    @SuppressWarnings("unchecked")
    public T set(int index, T element) throws ItemNotFound, IsEmpty {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        // Verifica existencia
        int idx = indexOf(element);
        if (idx < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + element);
        }
        T old = (T) elements[index];
        elements[index] = element;
        return old;
    }

    // * Elimina el elemento en la posición indicada.
    @SuppressWarnings("unchecked")
    public T remove(int index) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        if (index < 0 || index >= size) {
            throw new ItemNotFound("Índice inválido: " + index);
        }
        T old = (T) elements[index];
        // Desplazar
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
        return old;
    }

    // * Elimina la primera ocurrencia del elemento dado.
    public boolean remove(T e) throws IsEmpty, ItemNotFound {
        if (size == 0) {
            throw new IsEmpty("La lista está vacía");
        }
        int idx = indexOf(e);
        if (idx < 0) {
            throw new ItemNotFound("Elemento no encontrado: " + e);
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

    // * Representación en cadena de los elementos.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // * Asegura la capacidad mínima para un nuevo elemento.
    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
    }
}
