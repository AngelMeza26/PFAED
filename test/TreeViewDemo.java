package test;

import tree.BPlusTree;
import tree.TreeView;
import modelo.Record;

public class TreeViewDemo {
    public static void main(String[] args) throws Exception {
        BPlusTree<Record> tree = new BPlusTree<>(4);
        // Insertar algunos registros
        tree.insert(new Record("P1","Prod1",10,"Loc1"));
        tree.insert(new Record("P2","Prod2",20,"Loc1"));
        tree.insert(new Record("P3","Prod3",30,"Loc2"));

        // Mostrar la vista
        new TreeView<>(tree).display();
    }
}