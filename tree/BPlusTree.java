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
    private static final int DEFAULT_ORDER = 4;
    private final int order;
    private Node root;

    /** Nodo base, ya accesible para TreeView */
    abstract class Node {
        ArrayList<T> keys = new ArrayList<>();
        abstract boolean isLeaf();
    }

    /** Nodo interno */
    class InternalNode extends Node {
        ArrayList<Node> children = new ArrayList<>();
        @Override boolean isLeaf() { return false; }
    }

    /** Hoja del árbol */
    class LeafNode extends Node {
        ArrayList<T> values = new ArrayList<>();
        LeafNode next;
        @Override boolean isLeaf() { return true; }
    }

    /** Construye B+ Tree con orden por defecto */
    public BPlusTree() { this(DEFAULT_ORDER); }

    /** Construye B+ Tree con orden dado (>=3) */
    public BPlusTree(int order) {
        if (order < 3) throw new RuntimeException("Order must be >= 3");
        this.order = order;
        this.root = new LeafNode();
    }

    /** Permite a TreeView acceder a la raíz */
    public Node getRoot() {
        return root;
    }

    /** Inserta una clave, lanzando si ya existe 
     * @throws ItemNotFound 
     * @throws IsEmpty */
    public void insert(T key) throws ItemDuplicated, IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        int pos = 0;
        while (pos < leaf.keys.size() && key.compareTo(leaf.keys.get(pos)) > 0) {
            pos++;
        }
        if (pos < leaf.keys.size() && leaf.keys.get(pos).compareTo(key) == 0) {
            throw new ItemDuplicated("Clave duplicada: " + key);
        }
        leaf.keys.add(pos, key);
        leaf.values.add(pos, key);
        if (leaf.keys.size() > order - 1) {
            splitLeaf(leaf);
        }
    }

    private LeafNode findLeaf(Node node, T key) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) return (LeafNode) node;
        InternalNode in = (InternalNode) node;
        int i = 0;
        while (i < in.keys.size() && key.compareTo(in.keys.get(i)) >= 0) i++;
        return findLeaf(in.children.get(i), key);
    }

    private void splitLeaf(LeafNode leaf) throws ItemDuplicated, IsEmpty, ItemNotFound {
        int mid = order / 2;
        LeafNode sibling = new LeafNode();
        for (int i = mid; i < leaf.keys.size(); i++) {
            sibling.keys.add(leaf.keys.get(i));
            sibling.values.add(leaf.values.get(i));
        }
        while (leaf.keys.size() > mid) {
            leaf.keys.remove(mid);
            leaf.values.remove(mid);
        }
        sibling.next = leaf.next;
        leaf.next = sibling;
        insertIntoParent(leaf, sibling.keys.get(0), sibling);
    }

    private void insertIntoParent(Node left, T key, Node right) throws ItemDuplicated, IsEmpty, ItemNotFound {
        if (left == root) {
            InternalNode nr = new InternalNode();
            nr.keys.add(key);
            nr.children.add(left);
            nr.children.add(right);
            root = nr;
            return;
        }
        InternalNode parent = findParent(root, left);
        int pos = 0;
        while (pos < parent.keys.size() && key.compareTo(parent.keys.get(pos)) > 0) {
            pos++;
        }
        parent.keys.add(pos, key);
        parent.children.add(pos + 1, right);
        if (parent.children.size() > order) {
            splitInternal(parent);
        }
    }

    private InternalNode findParent(Node cur, Node child) throws IsEmpty, ItemNotFound {
        if (!cur.isLeaf()) {
            InternalNode in = (InternalNode) cur;
            for (int i = 0; i < in.children.size(); i++) {
                if (in.children.get(i) == child) {
                    return in;
                }
            }
            for (int i = 0; i < in.children.size(); i++) {
                InternalNode p = findParent(in.children.get(i), child);
                if (p != null) return p;
            }
        }
        return null;
    }

    private void splitInternal(InternalNode node) throws ItemDuplicated, IsEmpty, ItemNotFound {
        int mid = order / 2;
        T upKey = node.keys.get(mid);
        InternalNode sibling = new InternalNode();
        for (int i = mid + 1; i < node.keys.size(); i++) {
            sibling.keys.add(node.keys.get(i));
        }
        for (int i = mid + 1; i < node.children.size(); i++) {
            sibling.children.add(node.children.get(i));
        }
        while (node.keys.size() > mid) {
            node.keys.remove(mid);
        }
        while (node.children.size() > mid + 1) {
            node.children.remove(node.children.size() - 1);
        }
        insertIntoParent(node, upKey, sibling);
    }

    /** Verifica existencia 
     * @throws ItemNotFound 
     * @throws IsEmpty */
    public boolean contains(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i).compareTo(key) == 0) return true;
        }
        return false;
    }

    /** Elimina sin rebalancear 
     * @throws ItemNotFound 
     * @throws IsEmpty */
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

    /** Búsqueda por rango 
     * @throws ItemNotFound 
     * @throws IsEmpty 
     * @throws ItemDuplicated */
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

    /** Imprime estructura 
     * @throws ItemNotFound 
     * @throws IsEmpty */
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
