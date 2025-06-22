package graph;

import exceptions.ItemNotFound;

/**
 * Representa una arista en un grafo ponderado dirigido o no dirigido,
 * para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * @param <E> tipo de dato en vértices, debe ser Comparable para permitir ordenamientos.
 */
public class Edge<E extends Comparable<E>> implements Comparable<Edge<E>> {
    private final Vertex<E> destination;  // vértice destino de la arista
    private final double weight;           // peso de la arista (distancia, costo, etc.)
    private EdgeLabel label;               // estado para recorridos (UNEXPLORED, DISCOVERY, BACK)

    /**
     * Etiquetas posibles para una arista durante DFS o BFS.
     */
    public enum EdgeLabel {
        UNEXPLORED,
        DISCOVERY,
        BACK
    }

    /**
     * Construye una arista hacia el vértice dado con peso.
     * @param destination vértice destino (no null)
     * @param weight peso de la arista (>= 0)
     * @throws ItemNotFound si destination es null
     */
    public Edge(Vertex<E> destination, double weight) throws ItemNotFound {
        if (destination == null) {
            throw new ItemNotFound("Destino de arista no puede ser null");
        }
        if (weight < 0) {
            throw new ItemNotFound("Peso de arista inválido: " + weight);
        }
        this.destination = destination;
        this.weight = weight;
        this.label = EdgeLabel.UNEXPLORED;
    }

    /**
     * Construye una arista hacia el vértice dado con peso 1.0 por defecto.
     * @throws ItemNotFound si destination es null
     */
    public Edge(Vertex<E> destination) throws ItemNotFound {
        this(destination, 1.0);
    }

    public Vertex<E> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public EdgeLabel getLabel() {
        return label;
    }

    /**
     * Cambia la etiqueta de la arista.
     * @param label nueva etiqueta (no null)
     * @throws ItemNotFound si label es null
     */
    public void setLabel(EdgeLabel label) throws ItemNotFound {
        if (label == null) {
            throw new ItemNotFound("Etiqueta de arista no puede ser null");
        }
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge<?>)) return false;
        Edge<?> other = (Edge<?>) obj;
        return destination.equals(other.destination)
            && Double.compare(weight, other.weight) == 0;
    }

    @Override
    public int compareTo(Edge<E> other) {
        int cmp = Double.compare(this.weight, other.weight);
        if (cmp != 0) return cmp;
        return this.destination.compareTo(other.destination);
    }

    @Override
    public String toString() {
        return destination.getData() + "(" + weight + ") [" + label + "]";
    }
}