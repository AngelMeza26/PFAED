package sistema;

import tree.BPlusTree;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import graph.GraphLink;
import list.LinkedList;
import list.HashTable;
import modelo.Item;

/**
 * InventorySystem para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Agrupa estructuras:
 * - B+ Tree para categorías de ítems,
 * - Grafo para ubicaciones,
 * - HashTable para búsqueda rápida.
 */
public class InventorySystem {
    private final BPlusTree<Item> bPlusTree;
    private final GraphLink<String> graph;
    private final HashTable<String, Item> hashTable;

    /**
     * Inicializa el sistema con estructuras vacías.
     * @throws ItemDuplicated 
     */
    public InventorySystem() throws ItemDuplicated { //inicializamos las ESTRUC DATOS
        bPlusTree = new BPlusTree<>();
        graph     = new GraphLink<>();
        hashTable = new HashTable<>();
    }

    /**
     * Añade un ítem al sistema:
     * - lo inserta en el B+ Tree,
     * - en la tabla hash,
     * - y registra su ubicación en el grafo si aún no está.
     * @throws ItemDuplicated 
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void addItem(Item item) throws IsEmpty, ItemNotFound, ItemDuplicated { //INSERTAMOS EN LOS 3 TIPOS DE ED
        // 1) Árbol B+
        bPlusTree.insert(item);
        // 2) HashTable
        hashTable.put(item.getCode(), item);
        // 3) Grafo de ubicaciones
        try {
            graph.addVertex(item.getLocation());
        } catch (ItemDuplicated ignored) {
            // la ubicación ya estaba presente, seguimos
        }
    }

    /**
     * Elimina un ítem por su código.
     * @param code código del ítem a borrar
     * @return true si el ítem existía y fue eliminado
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    // CUANDO ELIMINAMOS TAMBIE MMANMOS A LAS 3 METODOS PARA EIMINARLOS CN RESPECTUVOS AE
    public boolean removeItem(String code) throws IsEmpty, ItemNotFound {
        Item it = hashTable.get(code);
        if (it == null) {
            return false;
        }
        bPlusTree.delete(it);
        hashTable.remove(code);
        return true;
    }

    /**
     * Recupera un ítem por su código.
     * @param code código del ítem
     * @throws ItemNotFound si no existe
     * @throws IsEmpty 
     */
    //ENCONTRA UN IREM EN TABKAS HASH
    public Item getItem(String code) throws ItemNotFound, IsEmpty {
        Item it = hashTable.get(code);
        if (it == null) {
            throw new ItemNotFound("Ítem no encontrado: " + code);
        }
        return it;
    }

    /** Muestra por consola la estructura del B+ Tree. 
     * @throws ItemNotFound 
     * @throws IsEmpty */
    public void displayTree() throws IsEmpty, ItemNotFound {// AYUDA A IMPIMIR LA ESTRUCTURA JERRAQUICA
        bPlusTree.display();
    }
    /** Muestra por consola la representación del grafo de ubicaciones. */
    public void displayGraph() {// MUESTRA LA ESTRUCTURA DEL GRAFO
        System.out.println(graph);
    }

    /**
     * Abre o cierra una ruta (arista) entre dos ubicaciones.
     * @param edge  en formato "NodoA-NodoB"
     * @param enable true para crear la arista, false para eliminarla
     */
    public void toggleEdge(String edge, boolean enable) {//ACTIVA O DESACTVA LAS CONEXIOBES DE LA UBICAION EN EL ALMACEN
        String[] parts = edge.split("-"); //ANALIZA LA CADEN EDGE
        if (parts.length != 2) return;

        try {
            if (enable) {
                graph.addEdge(parts[0], parts[1], 1.0);
            } else {
                graph.removeEdge(parts[0], parts[1]);
            }
        } catch (Exception e) {
            System.out.println("Error en toggleEdge: " + e.getMessage());
        }
    }

    /**
     * Simula un escenario de optimización de rutas entre "Entrada" y "Salida"
     * usando el método shortestPath de GraphLink.
     */
    public void simulate() { // SIMULA UNA OPTIMIZACION DE RUTA DENTRO DEL ALAMACEN
        try {
            LinkedList<String> path = graph.shortestPath("Entrada", "Salida");// ACTIA EL DISTRAC O EL BFS, DFS
            System.out.println("Ruta óptima: " + path);
        } catch (Exception e) {
            System.out.println("Error al simular ruta: " + e.getMessage());
        }
    }
}
