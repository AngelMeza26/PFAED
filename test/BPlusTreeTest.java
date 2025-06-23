package test;

import tree.BPlusTree;
import modelo.Location;
import modelo.Category;
import modelo.Item;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;

/**
 * Pruebas unitarias para BPlusTree, Location y Category.
 * Inserta, busca, elimina, rango y display.
 */
public class BPlusTreeTest {
    public static void main(String[] args) {
        try {
            // 1. Test básico de BPlusTree<Record>
            System.out.println("=== BPlusTree<Record> Básico ===");
            BPlusTree<Item> tree = new BPlusTree<>(4);
            Item a = new Item("P1","Prod1",10,"Loc1");
            Item b = new Item("P2","Prod2",20,"Loc1");
            Item c = new Item("P3","Prod3",30,"Loc2");
            tree.insert(a);
            tree.insert(b);
            tree.insert(c);
            System.out.println("Contains P2? " + tree.contains(b)); // true
            tree.display();
            System.out.println();

            // 2. Prueba delete
            System.out.println("=== Delete ===");
            tree.delete(b);
            System.out.println("Después de delete(P2):");
            tree.display();
            System.out.println("Contains P2? " + tree.contains(b)); // false
            System.out.println();

            // 3. Prueba rangeSearch
            System.out.println("=== rangeSearch(P1..P3) ===");
            tree.insert(b);
            tree.insert(new Item("P4","Prod4",40,"Loc2"));
            var rango = tree.rangeSearch(a, c);
            System.out.println("Claves en rango P1..P3: " + rango);
            System.out.println();

            // 4. Test Location
            System.out.println("=== Location Test ===");
            Location loc = new Location("AlmacenA");
            loc.addItem(a);
            loc.addItem(c);
            System.out.print("Loc.contains(P3)? ");
            System.out.println(loc.contains(c)); // true
            loc.displayItemsTree();
            System.out.println();

            // 5. Test Category
            System.out.println("=== Category Test ===");
            Category cat = new Category("Electrónica");
            cat.addItem(b);
            cat.addItem(new Item("P5","Prod5",50,"Loc3"));
            System.out.print("Cat.contains(P2)? ");
            System.out.println(cat.contains(b)); // true
            cat.displayItemsTree();
            System.out.println();

        } catch (ItemDuplicated e) {
            System.err.println("Duplicado: " + e.getMessage());
        } catch (ItemNotFound e) {
            System.err.println("No encontrado: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e);
        }
    }
}
