package test;

import graph.GraphLink;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import exceptions.IsEmpty;

import java.util.Arrays;

/**
 * Prueba unitaria para GraphLink:
 * - Añadir/eliminar vértices
 * - Añadir/eliminar aristas
 * - Mostrar grafo
 * - Calcular rutas con Dijkstra
 */
public class GraphTest {
    public static void main(String[] args) {
        GraphLink<String> graph = new GraphLink<>();

        System.out.println("=== Iniciando GraphLink Test ===\n");

        // 1. Añadir vértices
        try {
            System.out.println("[Añadir Vértices]");
            Arrays.asList("Entrada", "A", "B", "C", "Salida")
                  .forEach(v -> {
                      try {
                          graph.addVertex(v);
                          System.out.println("Vértice agregado: " + v);
                      } catch (ItemDuplicated e) {
                          System.out.println("Error al agregar vértice duplicado: " + v);
                      }
                  });
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error en añadir vértices: " + e.getMessage());
        }

        // 2. Añadir aristas
        System.out.println("[Añadir Aristas]");
        try {
            graph.addEdge("Entrada", "A", 5.0);
            graph.addEdge("Entrada", "B", 10.0);
            graph.addEdge("A", "C", 3.0);
            graph.addEdge("B", "C", 1.0);
            graph.addEdge("C", "Salida", 2.0);
            System.out.println("Aristas añadidas con pesos:");
            System.out.println("  Entrada->A (5.0)");
            System.out.println("  Entrada->B (10.0)");
            System.out.println("  A->C (3.0)");
            System.out.println("  B->C (1.0)");
            System.out.println("  C->Salida (2.0)\n");
        } catch (Exception e) {
            System.err.println("Error al añadir aristas: " + e.getMessage());
        }

        // 3. Mostrar grafo completo
        System.out.println("[Grafo Actual]");
        System.out.println(graph);
        
        // 4. Calcular ruta óptima Entrada->Salida
        System.out.println("[Ruta óptima Entrada->Salida]");
        try {
            var path = graph.shortestPath("Entrada", "Salida");
            System.out.println("Camino: " + path);
        } catch (Exception e) {
            System.err.println("Error en shortestPath: " + e.getMessage());
        }
        System.out.println();

        // 5. Eliminar una arista y recalcular
        System.out.println("[Eliminar arista B->C y recalcular]");
        try {
            graph.removeEdge("B", "C");
            System.out.println("Arista B->C eliminada.");
            System.out.println(graph);
            var path2 = graph.shortestPath("Entrada", "Salida");
            System.out.println("Nuevo camino: " + path2);
        } catch (Exception e) {
            System.err.println("Error al eliminar arista o recalcular: " + e.getMessage());
        }
        System.out.println();

        // 6. Eliminar vértice intermedio y mostrar
        System.out.println("[Eliminar vértice C y todas sus aristas]");
        try {
            graph.removeVertex("C");
            System.out.println("Vértice C eliminado.");
            System.out.println(graph);
        } catch (Exception e) {
            System.err.println("Error al eliminar vértice: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== Fin de GraphLink Test ===");
    }
}
