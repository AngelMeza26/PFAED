package graph;

import exceptions.ItemNotFound;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import list.LinkedList;

/**
 * Vértice genérico para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Cada vértice almacena un dato y una lista de aristas salientes.
 *
 * @param <E> tipo de datos, debe ser Comparable para ordenamientos
 */
public class Vertex<E extends Comparable<E>> implements Comparable<Vertex<E>> {
    private final E data;
    private final LinkedList<Edge<E>> adjList;
    private VertexLabel label;

    /** Estados posibles de un vértice durante la exploración */
    public enum VertexLabel {
        UNVISITED,
        VISITING,
        VISITED
    }

    /**
     * Construye un vértice con el dato dado.
     * @param data valor del vértice
     */
    public Vertex(E data) {
        this.data = data;
        this.adjList = new LinkedList<>();
        this.label = VertexLabel.UNVISITED;
    }

    /** @return el dato asociado a este vértice */
    public E getData() {
        return data;
    }

    /** @return la etiqueta de recorrido actual */
    public VertexLabel getLabel() {
        return label;
    }

    /** @param label nueva etiqueta de recorrido */
    public void setLabel(VertexLabel label) {
        this.label = label;
    }

    /**
     * Añade una arista saliente a este vértice.
     * @param edge arista a agregar
     * @throws ItemDuplicated si ya existe una arista al mismo destino
     * @throws IsEmpty nunca ocurre aquí, pero declarado por la firma de LinkedList.add
     * @throws ItemNotFound 
     */
    public void addEdge(Edge<E> edge) throws ItemDuplicated, IsEmpty, ItemNotFound {
        // Prevent duplicate edges to the same destination
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).getDestination().equals(edge.getDestination())) {
                throw new ItemDuplicated("Arista duplicada hacia: " + edge.getDestination().getData());
            }
        }
        adjList.add(edge);
    }

    /**
     * Elimina la arista que va de este vértice a dest.
     * @param dest vértice destino de la arista a quitar
     * @throws IsEmpty si no hay aristas en este vértice
     * @throws ItemNotFound si no existe arista hacia dest
     */
    public void removeEdgeTo(Vertex<E> dest) throws IsEmpty, ItemNotFound {
        if (adjList.isEmpty()) {
            throw new IsEmpty("No hay aristas que eliminar en vértice: " + data);
        }
        for (int i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).getDestination().equals(dest)) {
                adjList.remove(i);
                return;
            }
        }
        throw new ItemNotFound("Arista no encontrada hacia: " + dest.getData());
    }

    /** @return la lista de aristas salientes */
    public LinkedList<Edge<E>> getAdjList() {
        return adjList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertex<?>)) return false;
        Vertex<?> other = (Vertex<?>) obj;
        return data == null ? other.data == null : data.equals(other.data);
    }

    @Override
    public int compareTo(Vertex<E> o) {
        if (data == null && o.data == null) return 0;
        if (data == null) return -1;
        if (o.data == null) return 1;
        return data.compareTo(o.data);
    }

    @Override
    public String toString() {
        return data + " -> " + adjList.toString();
    }
}
