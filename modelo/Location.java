package modelo;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import tree.BPlusTree;

/**
 * Representa una ubicación en el almacén, con su propio B+ Tree de ítems.
 * Cada Location mantiene los ítems presentes en ella, indexados en un B+ Tree para búsquedas y recorridos eficientes.
 */
public class Location {
    private final String name;
    private final BPlusTree<Item> itemsTree;

    /**
     * Construye una nueva ubicación con nombre dado.
     * @param name identificador de la ubicación (p. ej. “AlmacenA”)
     */
    public Location(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacío");
        }
        this.name = name;
        this.itemsTree = new BPlusTree<>();
    }

    /** @return el nombre de esta ubicación */
    public String getName() {
        return name;
    }

    /**
     * Añade un ítem a esta ubicación (inserción en el B+ Tree).
     * @param item ítem a insertar
     * @throws ItemDuplicated 
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void addItem(Item item) throws IsEmpty, ItemNotFound, ItemDuplicated {
        itemsTree.insert(item);
    }

    /**
     * Elimina un ítem de esta ubicación (borrado en el B+ Tree).
     * @param item ítem a eliminar
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void removeItem(Item item) throws IsEmpty, ItemNotFound {
        itemsTree.delete(item);
    }

    /**
     * Verifica si un ítem existe en esta ubicación.
     * @param item ítem a buscar
     * @return true si está presente
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public boolean contains(Item item) throws IsEmpty, ItemNotFound {
        return itemsTree.contains(item);
    }

    /**
     * Muestra en consola la estructura B+ Tree de los ítems en esta ubicación.
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void displayItemsTree() throws IsEmpty, ItemNotFound {
        System.out.println("Ubicación “" + name + "” – B+ Tree de ítems:");
        itemsTree.display();
    }

    @Override
    public String toString() {
        return "Location{name='" + name + "'}";
    }
}
