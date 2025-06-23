package test;

import graph.GraphLink;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import exceptions.IsEmpty;

import java.util.Arrays;

/**
 * Prueba unitaria para la clase GraphLink.
 * Se testean operaciones clave:
 * - Añadir y eliminar vértices
 * - Añadir y eliminar aristas
 * - Mostrar el grafo
 * - Calcular rutas óptimas con el algoritmo de Dijkstra
 */
public class GraphTest {
    public static void main(String[] args) {
        // Se crea una instancia de grafo dirigido y ponderado de tipo String
        GraphLink<String> graph = new GraphLink<>();

        System.out.println("=== Iniciando GraphLink Test ===\n");

        // 1. Añadir vértices
        try {
            System.out.println("[Añadir Vértices]");
            // Se agregan los nodos al grafo representando diferentes puntos
            Arrays.asList("Entrada", "A", "B", "C", "Salida")
                  .forEach(v -> {
                      try {
                          graph.addVertex(v); // Agrega vértice
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
            // Se añaden conexiones entre los vértices con pesos asociados
            graph.addEdge("Entrada", "A", 5.0);
            graph.addEdge("Entrada", "B", 10.0);
            graph.addEdge("A", "C", 3.0);
            graph.addEdge("B", "C", 1.0);
            graph.addEdge("C", "Salida", 2.0);

            // Visualización de las aristas creadas
            System.out.println("Aristas añadidas con pesos:");
            System.out.println("  Entrada->A (5.0)");
            System.out.println("  Entrada->B (10.0)");
            System.out.println("  A->C (3.0)");
            System.out.println("  B->C (1.0)");
            System.out.println("  C->Salida (2.0)\n");
        } catch (Exception e) {
            System.err.println("Error al añadir aristas: " + e.getMessage());
        }

        // 3. Mostrar el grafo actual
        System.out.println("[Grafo Actual]");
        System.out.println(graph); // Imprime representación interna del grafo

        // 4. Calcular ruta más corta desde "Entrada" hasta "Salida"
        System.out.println("[Ruta óptima Entrada->Salida]");
        try {
            var path = graph.shortestPath("Entrada", "Salida");
            System.out.println("Camino: " + path); // Ruta óptima según Dijkstra
        } catch (Exception e) {
            System.err.println("Error en shortestPath: " + e.getMessage());
        }
        System.out.println();

        // 5. Eliminar una arista y recalcular la ruta óptima
        System.out.println("[Eliminar arista B->C y recalcular]");
        try {
            graph.removeEdge("B", "C"); // Elimina conexión directa entre B y C
            System.out.println("Arista B->C eliminada.");
            System.out.println(graph); // Muestra el grafo actualizado

            var path2 = graph.shortestPath("Entrada", "Salida");
            System.out.println("Nuevo camino: " + path2); // Nueva ruta tras eliminar la arista
        } catch (Exception e) {
            System.err.println("Error al eliminar arista o recalcular: " + e.getMessage());
        }
        System.out.println();

        // 6. Eliminar un vértice intermedio y visualizar el grafo
        System.out.println("[Eliminar vértice C y todas sus aristas]");
        try {
            graph.removeVertex("C"); // Elimina el nodo C y sus conexiones
            System.out.println("Vértice C eliminado.");
            System.out.println(graph); // Grafo sin el nodo C
        } catch (Exception e) {
            System.err.println("Error al eliminar vértice: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== Fin de GraphLink Test ===");
    }
}