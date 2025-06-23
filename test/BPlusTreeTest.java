package test;

import tree.BPlusTree;
import modelo.Location;
import modelo.Category;
import modelo.Item;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;

/**
 * Pruebas unitarias para validar el funcionamiento de BPlusTree, Location y Category.
 * Se prueban inserciones, búsquedas, eliminaciones, búsquedas por rango y visualización.
 */
public class BPlusTreeTest {
    public static void main(String[] args) {
        try {
            // 1. Test básico de BPlusTree<Item>
            System.out.println("=== BPlusTree<Record> Básico ===");
            BPlusTree<Item> tree = new BPlusTree<>(4); // Árbol B+ de orden 4
            Item a = new Item("P1", "Prod1", 10, "Loc1");
            Item b = new Item("P2", "Prod2", 20, "Loc1");
            Item c = new Item("P3", "Prod3", 30, "Loc2");

            // Insertamos tres ítems en el árbol
            tree.insert(a);
            tree.insert(b);
            tree.insert(c);

            // Verificamos si el ítem b ("P2") está contenido
            System.out.println("Contains P2? " + tree.contains(b)); // true

            // Mostramos la estructura actual del árbol
            tree.display();
            System.out.println();

            // 2. Prueba de eliminación
            System.out.println("=== Delete ===");
            tree.delete(b); // Eliminamos el ítem b ("P2")
            System.out.println("Después de delete(P2):");
            tree.display(); // Mostramos el árbol después de la eliminación
            System.out.println("Contains P2? " + tree.contains(b)); // false
            System.out.println();

            // 3. Prueba de búsqueda por rango
            System.out.println("=== rangeSearch(P1..P3) ===");
            tree.insert(b); // Volvemos a insertar b para la prueba de rango
            tree.insert(new Item("P4", "Prod4", 40, "Loc2")); // Insertamos un nuevo ítem

            // Buscamos todos los ítems con claves entre P1 y P3 (inclusive)
            var rango = tree.rangeSearch(a, c);
            System.out.println("Claves en rango P1..P3: " + rango);
            System.out.println();

            // 4. Prueba con clase Location (almacén)
            System.out.println("=== Location Test ===");
            Location loc = new Location("AlmacenA");
            loc.addItem(a);
            loc.addItem(c);

            // Verificamos si el ítem c está en esa ubicación
            System.out.print("Loc.contains(P3)? ");
            System.out.println(loc.contains(c)); // true

            // Mostramos los ítems almacenados en la ubicación
            loc.displayItemsTree();
            System.out.println();

            // 5. Prueba con clase Category (categoría de productos)
            System.out.println("=== Category Test ===");
            Category cat = new Category("Electrónica");
            cat.addItem(b);
            cat.addItem(new Item("P5", "Prod5", 50, "Loc3"));

            // Verificamos si la categoría contiene el ítem b
            System.out.print("Cat.contains(P2)? ");
            System.out.println(cat.contains(b)); // true

            // Mostramos los ítems clasificados dentro de esta categoría
            cat.displayItemsTree();
            System.out.println();

        } catch (ItemDuplicated e) {
            // Captura de error si se intenta insertar un ítem duplicado
            System.err.println("Duplicado: " + e.getMessage());
        } catch (ItemNotFound e) {
            // Captura de error si no se encuentra un ítem
            System.err.println("No encontrado: " + e.getMessage());
        } catch (Exception e) {
            // Captura de cualquier otro error inesperado
            System.err.println("Error inesperado: " + e);
        }
    }
}