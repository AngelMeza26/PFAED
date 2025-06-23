package test;

import sistema.InventorySystem;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import modelo.Item;

/**
 * Clase de prueba para la funcionalidad de simular escenario
 * en el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 */
public class SimulationTest {
    public static void main(String[] args) throws IsEmpty, ItemNotFound, ItemDuplicated {
        InventorySystem system = new InventorySystem();

        // Agregar ítems para crear nodos de ubicación
        system.addItem(new Item("I1", "Item1", 10, "Entrada"));
        system.addItem(new Item("I2", "Item2", 5, "AlmacenA"));
        system.addItem(new Item("I3", "Item3", 8, "AlmacenB"));
        system.addItem(new Item("I4", "Item4", 12, "Salida"));

        // Configurar rutas entre ubicaciones
        system.toggleEdge("Entrada-AlmacenA", true);
        system.toggleEdge("Entrada-AlmacenB", true);
        system.toggleEdge("AlmacenA-Salida", true);
        system.toggleEdge("AlmacenB-Salida", true);

        // Ejecutar simulación: debería calcular ruta óptima de Entrada a Salida
        System.out.println("--- Resultado de simulación ---");
        system.simulate();
    }
}
