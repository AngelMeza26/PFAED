package tree;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import list.ArrayList;

/**
 * B+ Tree genérico para Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Permite inserción, búsqueda, eliminación básica y consultas por rango.
 * Las hojas están enlazadas para búsquedas por rango.
 * @param <T> tipo de clave, debe ser Comparable
 */
public class BPlusTree<T extends Comparable<T>> {
    private static final int DEFAULT_ORDER = 4;
    private final int order;
    private Node root;

    private abstract class Node {
        ArrayList<T> keys = new ArrayList<>();
        int keyCount() { return keys.size(); }
        abstract boolean isLeaf();
    }

    private class InternalNode extends Node {
        ArrayList<Node> children = new ArrayList<>();
        @Override boolean isLeaf() { return false; }
    }

    private class LeafNode extends Node {
        ArrayList<T> values = new ArrayList<>();
        LeafNode next;
        @Override boolean isLeaf() { return true; }
    }

    public BPlusTree() { this(DEFAULT_ORDER); }
    public BPlusTree(int order) {
        if (order < 3) throw new IllegalArgumentException("Order must be >= 3");
        this.order = order;
        root = new LeafNode();
    }

    public void insert(T key) throws IsEmpty, ItemNotFound, ItemDuplicated {
        LeafNode leaf = findLeaf(root, key);
        insertIntoLeaf(leaf, key);
        if (leaf.keyCount() > order - 1) splitLeaf(leaf);
    }

    private LeafNode findLeaf(Node node, T key) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) return (LeafNode) node;
        InternalNode in = (InternalNode) node;
        int idx = 0;
        while (idx < in.keyCount() && key.compareTo(in.keys.get(idx)) >= 0) idx++;
        return findLeaf(in.children.get(idx), key);
    }

    private void insertIntoLeaf(LeafNode leaf, T key) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int pos = 0;
        while (pos < leaf.keyCount() && key.compareTo(leaf.keys.get(pos)) > 0) pos++;
        leaf.keys.add(pos, key);
        leaf.values.add(pos, key);
    }

    private void splitLeaf(LeafNode leaf) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int mid = order / 2;
        LeafNode newLeaf = new LeafNode();
        // mover claves y valores
        for (int i = mid; i < leaf.keyCount(); i++) {
            newLeaf.keys.add(leaf.keys.get(i));
            newLeaf.values.add(leaf.values.get(i));
        }
        // borrar movidos de hoja original
        while (leaf.keyCount() > mid) {
            leaf.keys.remove(mid);
            leaf.values.remove(mid);
        }
        // enlazar hojas
        newLeaf.next = leaf.next;
        leaf.next = newLeaf;
        // insertar en padre
        insertIntoParent(leaf, newLeaf.keys.get(0), newLeaf);
    }

    private void insertIntoParent(Node left, T key, Node right) throws ItemDuplicated, ItemNotFound, IsEmpty {
        if (left == root) {
            InternalNode newRoot = new InternalNode();
            newRoot.keys.add(key);
            newRoot.children.add(left);
            newRoot.children.add(right);
            root = newRoot;
            return;
        }
        InternalNode parent = findParent(root, left);
        int idx = 0;
        while (idx < parent.keyCount() && key.compareTo(parent.keys.get(idx)) >= 0) idx++;
        parent.keys.add(idx, key);
        parent.children.add(idx+1, right);
        if (parent.children.size() > order) splitInternal(parent);
    }

    private InternalNode findParent(Node current, Node child) throws IsEmpty, ItemNotFound {
        if (!current.isLeaf()) {
            InternalNode in = (InternalNode) current;
            for (int i = 0; i < in.children.size(); i++) {
                if (in.children.get(i) == child) return in;
            }
            for (int i = 0; i < in.children.size(); i++) {
                InternalNode parent = findParent(in.children.get(i), child);
                if (parent != null) return parent;
            }
        }
        return null;
    }

    private void splitInternal(InternalNode node) throws IsEmpty, ItemNotFound, ItemDuplicated {
        int mid = order / 2;
        T upKey = node.keys.get(mid);
        InternalNode rightNode = new InternalNode();
        // mover claves
        for (int i = mid+1; i < node.keyCount(); i++) {
            rightNode.keys.add(node.keys.get(i));
        }
        // mover hijos
        for (int i = mid+1; i < node.children.size(); i++) {
            rightNode.children.add(node.children.get(i));
        }
        // recortar nodo original
        while (node.keyCount() > mid) node.keys.remove(mid);
        while (node.children.size() > mid+1) node.children.remove(node.children.size()-1);
        insertIntoParent(node, upKey, rightNode);
    }

    public boolean contains(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keyCount(); i++) {
            if (leaf.keys.get(i).equals(key)) return true;
        }
        return false;
    }

    public ArrayList<T> rangeSearch(T from, T to) throws IsEmpty, ItemNotFound, ItemDuplicated {
        ArrayList<T> result = new ArrayList<>();
        LeafNode leaf = findLeaf(root, from);
        while (leaf != null) {
            for (int i = 0; i < leaf.keyCount(); i++) {
                T k = leaf.keys.get(i);
                if (k.compareTo(from) >= 0 && k.compareTo(to) <= 0) {
                    result.add(k);
                } else if (k.compareTo(to) > 0) {
                    return result;
                }
            }
            leaf = leaf.next;
        }
        return result;
    }

    public void delete(T key) throws IsEmpty, ItemNotFound {
        LeafNode leaf = findLeaf(root, key);
        for (int i = 0; i < leaf.keyCount(); i++) {
            if (leaf.keys.get(i).equals(key)) {
                leaf.keys.remove(i);
                leaf.values.remove(i);
                return;
            }
        }
    }

    public void display() throws IsEmpty, ItemNotFound {
        display(root, "");
    }

    private void display(Node node, String indent) throws IsEmpty, ItemNotFound {
        if (node.isLeaf()) {
            System.out.println(indent + "Leaf: " + node.keys);
        } else {
            InternalNode in = (InternalNode) node;
            System.out.println(indent + "Internal: " + in.keys);
            for (int i = 0; i < in.children.size(); i++) {
                display(in.children.get(i), indent + "    ");
            }
        }
    }
}
