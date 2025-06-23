package modelo;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import tree.BPlusTree;

/**
 * Representa una categoría de ítems en el almacén, con su propio B+ Tree.
 * Cada Category mantiene los ítems pertenecientes a ella, indexados
 * en un B+ Tree para búsquedas y recorridos eficientes.
 */
public class Category {
    private final String name;
    private final BPlusTree<Item> itemsTree;

    /**
     * Construye una nueva categoría con nombre dado.
     * @param name identificador de la categoría (p. ej. “Electrónica”)
     */
    public Category(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío");
        }
        this.name = name;
        this.itemsTree = new BPlusTree<>();
    }

    /** @return el nombre de esta categoría */
    public String getName() {
        return name;
    }

    /**
     * Añade un ítem a esta categoría (inserción en el B+ Tree).
     * @param item ítem a insertar
     * @throws ItemDuplicated 
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void addItem(Item item) throws IsEmpty, ItemNotFound, ItemDuplicated {
        itemsTree.insert(item);
    }

    /**
     * Elimina un ítem de esta categoría (borrado en el B+ Tree).
     * @param item ítem a eliminar
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void removeItem(Item item) throws IsEmpty, ItemNotFound {
        itemsTree.delete(item);
    }

    /**
     * Verifica si un ítem pertenece a esta categoría.
     * @param item ítem a buscar
     * @return true si está presente
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public boolean contains(Item item) throws IsEmpty, ItemNotFound {
        return itemsTree.contains(item);
    }

    /**
     * Muestra en consola la estructura B+ Tree de los ítems en esta categoría.
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void displayItemsTree() throws IsEmpty, ItemNotFound {
        System.out.println("Categoría “" + name + "” – B+ Tree de ítems:");
        itemsTree.display();
    }

    @Override
    public String toString() {
        return "Category{name='" + name + "'}";
    }
}
