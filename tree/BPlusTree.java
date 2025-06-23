package tree;

import list.ArrayList;
import exceptions.ItemDuplicated;
import exceptions.IsEmpty;
import exceptions.ItemNotFound;

/**
 * B+ Tree genérico para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Cada nodo almacena hasta 'order' claves; las hojas están encadenadas para búsquedas por rango.
 *
 * @param <T> tipo de clave, debe implementar Comparable<T>
 */
public class BPlusTree<T extends Comparable<T>> {
    private static final int DEFAULT_ORDER = 4; // Orden por defecto del árbol (máx claves por nodo)
    private final int order; // Orden definido por el usuario
    private Node root; // Raíz del árbol

    /** Nodo base, clase abstracta común para hojas e internos */
    abstract class Node {
        ArrayList<T> keys = new ArrayList<>(); // Claves contenidas en el nodo
        abstract boolean isLeaf(); // Método para saber si es hoja
    }

    /** Nodo interno que almacena punteros a otros nodos (hijos) */
    class InternalNode extends Node {
        ArrayList<Node> children = new ArrayList<>(); // Hijos de este nodo interno
        @Override boolean isLeaf() { return false; }
    }

    /** Nodo hoja que almacena claves y valores, y apunta a la siguiente hoja (para búsquedas por rango) */
    class LeafNode extends Node {
        ArrayList<T> values = new ArrayList<>(); // En este caso, los valores son iguales a las claves
        LeafNode next; // Apuntador a la siguiente hoja (lista enlazada)
        @Override boolean isLeaf() { return true; }
    }

    /** Constructor por defecto con orden predefinido */
    public BPlusTree() {
        this(DEFAULT_ORDER);
    }

    /** Constructor que permite definir el orden del árbol */
    public BPlusTree(int order) {
        if (order < 3) throw new RuntimeException("Order must be >= 3");
        this.order = order;
        this.root = new LeafNode(); // El árbol inicia con una hoja vacía como raíz
    }

    /** Retorna la raíz para visualización externa */
    public Node getRoot() {
        return root;
    }

    /**
     * Inserta una clave al árbol.
     * Si ya existe, lanza excepción.
     */
    public void insert(T key) throws ItemDuplicated, IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key); // Encuentra la hoja correspondiente
        int pos = 0;
        // Busca la posición de inserción ordenada
        while (pos < leaf.keys.size() && key.compareTo(leaf.keys.get(pos)) > 0) {
            pos++;
        }
        // Si ya existe la clave, lanza excepción
        if (pos < leaf.keys.size() && leaf.keys.get(pos).compareTo(key) == 0) {
            throw new ItemDuplicated("Clave duplicada: " + key);
        }
        // Inserta clave y valor en la posición correspondiente
        leaf.keys.add(pos, key);
        leaf.values.add(pos, key);
        // Si se sobrepasa la capacidad, se divide
        if (leaf.keys.size() > order - 1) {
            splitLeaf(leaf);
        }
    }

    /**
     * Encuentra la hoja que contendría la clave especificada
     */
    private LeafNode findLeaf(Node node, T key) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) return (LeafNode) node;
        InternalNode in = (InternalNode) node;
        int i = 0;
        while (i < in.keys.size() && key.compareTo(in.keys.get(i)) >= 0) i++;
        return findLeaf(in.children.get(i), key);
    }

    /**
     * Divide una hoja en dos cuando se excede el número de claves
     */
    private void splitLeaf(LeafNode leaf) throws ItemDuplicated, IsEmpty, ItemNotFound {
        int mid = order / 2;
        LeafNode sibling = new LeafNode();
        // Mueve la mitad superior de las claves/valores al nuevo hermano
        for (int i = mid; i < leaf.keys.size(); i++) {
            sibling.keys.add(leaf.keys.get(i));
            sibling.values.add(leaf.values.get(i));
        }
        // Elimina esas claves de la hoja original
        while (leaf.keys.size() > mid) {
            leaf.keys.remove(mid);
            leaf.values.remove(mid);
        }
        // Actualiza la lista enlazada de hojas
        sibling.next = leaf.next;
        leaf.next = sibling;
        // Inserta la clave promovida en el padre
        insertIntoParent(leaf, sibling.keys.get(0), sibling);
    }

    /**
     * Inserta una clave y nuevo hijo en el nodo padre del nodo dividido
     */
    private void insertIntoParent(Node left, T key, Node right) throws ItemDuplicated, IsEmpty, ItemNotFound {
        // Si se divide la raíz, se crea una nueva
        if (left == root) {
            InternalNode nr = new InternalNode();
            nr.keys.add(key);
            nr.children.add(left);
            nr.children.add(right);
            root = nr;
            return;
        }
        // Encuentra el padre y la posición adecuada
        InternalNode parent = findParent(root, left);
        int pos = 0;
        while (pos < parent.keys.size() && key.compareTo(parent.keys.get(pos)) > 0) {
            pos++;
        }
        parent.keys.add(pos, key);
        parent.children.add(pos + 1, right);
        // Si se sobrepasa la capacidad, se divide el nodo interno
        if (parent.children.size() > order) {
            splitInternal(parent);
        }
    }

    /**
     * Encuentra el nodo padre de un hijo dado (usado en inserción)
     */
    private InternalNode findParent(Node cur, Node child) throws IsEmpty, ItemNotFound {
        if (!cur.isLeaf()) {
            InternalNode in = (InternalNode) cur;
            for (int i = 0; i < in.children.size(); i++) {
                if (in.children.get(i) == child) return in;
            }
            for (int i = 0; i < in.children.size(); i++) {
                InternalNode p = findParent(in.children.get(i), child);
                if (p != null) return p;
            }
        }
        return null;
    }

    /**
     * Divide un nodo interno que ha superado su capacidad
     */
    private void splitInternal(InternalNode node) throws ItemDuplicated, IsEmpty, ItemNotFound {
        int mid = order / 2;
        T upKey = node.keys.get(mid); // Clave que se promoverá al padre
        InternalNode sibling = new InternalNode();
        // Mueve claves y punteros hijos al nuevo nodo
        for (int i = mid + 1; i < node.keys.size(); i++) {
            sibling.keys.add(node.keys.get(i));
        }
        for (int i = mid + 1; i < node.children.size(); i++) {
            sibling.children.add(node.children.get(i));
        }
        // Elimina las claves e hijos movidos del nodo original
        while (node.keys.size() > mid) {
            node.keys.remove(mid);
        }
        while (node.children.size() > mid + 1) {
            node.children.remove(node.children.size() - 1);
        }
        // Inserta la clave promovida al padre
        insertIntoParent(node, upKey, sibling);
    }

    /**
     * Verifica si la clave está presente en el árbol
     */
    public boolean contains(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i).compareTo(key) == 0) return true;
        }
        return false;
    }

    /**
     * Elimina una clave del árbol sin rebalancear
     */
    public void delete(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i).compareTo(key) == 0) {
                leaf.keys.remove(i);
                leaf.values.remove(i);
                return;
            }
        }
    }

    /**
     * Búsqueda por rango: devuelve todas las claves en el rango [from, to]
     */
    public ArrayList<T> rangeSearch(T from, T to) throws IsEmpty, ItemNotFound, ItemDuplicated {
        ArrayList<T> res = new ArrayList<>();
        LeafNode leaf = findLeaf(root, from);
        while (leaf != null) {
            for (int i = 0; i < leaf.keys.size(); i++) {
                T k = leaf.keys.get(i);
                if (k.compareTo(from) >= 0 && k.compareTo(to) <= 0) {
                    res.add(k);
                } else if (k.compareTo(to) > 0) {
                    return res;
                }
            }
            leaf = leaf.next;
        }
        return res;
    }

    /**
     * Imprime la estructura del árbol (para debug o visualización)
     */
    public void display() throws IsEmpty, ItemNotFound {
        displayNode(root, "");
    }

    private void displayNode(Node node, String indent) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) {
            System.out.println(indent + "Leaf: " + node.keys);
        } else {
            InternalNode in = (InternalNode) node;
            System.out.println(indent + "Internal: " + in.keys);
            for (int i = 0; i < in.children.size(); i++) {
                displayNode(in.children.get(i), indent + "    ");
            }
        }
    }
}