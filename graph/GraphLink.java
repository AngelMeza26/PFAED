
package graph;

import exceptions.ItemNotFound;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import list.LinkedList;
import graph.Vertex;
import graph.Edge;

/**
 * Grafo genérico ponderado para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Incluye rutas óptimas (Dijkstra), BFS, DFS, detección de ciclos, componentes conexas y zonas aisladas.
 * @param <E> tipo de datos de los vértices (Comparable)
 */
public class GraphLink<E extends Comparable<E>> {
    private final LinkedList<Vertex<E>> vertices;

    public GraphLink() {
        vertices = new LinkedList<>();
    }

    public void addVertex(E data) throws ItemDuplicated {
        Vertex<E> v = new Vertex<>(data);
        if (vertices.indexOf(v) >= 0) {
            throw new ItemDuplicated("Vértice ya existe: " + data);
        }
        vertices.add(v);
    }

    public void removeVertex(E data) throws IsEmpty, ItemNotFound {
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío");
        Vertex<E> v = findVertex(data);
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).removeEdgeTo(v);
        }
        vertices.remove(v);
    }

    public void addEdge(E src, E dest, double weight) throws ItemNotFound, ItemDuplicated, IsEmpty {
        Vertex<E> vSrc = findVertex(src);
        Vertex<E> vDest = findVertex(dest);
        Edge<E> edge = new Edge<>(vDest, weight);
        vSrc.addEdge(edge);
    }

    public void removeEdge(E src, E dest) throws ItemNotFound, IsEmpty {
        Vertex<E> vSrc = findVertex(src);
        Vertex<E> vDest = findVertex(dest);
        vSrc.removeEdgeTo(vDest);
    }

    public LinkedList<E> shortestPath(E origin, E destination) throws ItemNotFound, IsEmpty, ItemDuplicated {
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío");
        Vertex<E> src = findVertex(origin);
        Vertex<E> dst = findVertex(destination);
        int n = vertices.size();
        double[] dist = new double[n];
        Vertex<E>[] prev = new Vertex[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = vertices.get(i).equals(src) ? 0.0 : Double.POSITIVE_INFINITY;
            prev[i] = null;
            visited[i] = false;
        }

        for (int k = 0; k < n; k++) {
            int u = -1;
            double min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }
            if (u < 0) break;
            visited[u] = true;
            Vertex<E> vU = vertices.get(u);
            if (vU.equals(dst)) break;

            LinkedList<Edge<E>> adj = vU.getAdjList();
            for (int j = 0; j < adj.size(); j++) {
                Edge<E> e = adj.get(j);
                Vertex<E> vV = e.getDestination();
                int vIdx = vertices.indexOf(vV);
                if (vIdx < 0) continue;
                double alt = dist[u] + e.getWeight();
                if (alt < dist[vIdx]) {
                    dist[vIdx] = alt;
                    prev[vIdx] = vU;
                }
            }
        }

        LinkedList<E> path = new LinkedList<>();
        Vertex<E> step = dst;
        while (step != null) {
            path.add(0, step.getData());
            int idx = vertices.indexOf(step);
            step = (idx >= 0) ? prev[idx] : null;
        }
        return path;
    }

    private Vertex<E> findVertex(E data) throws ItemNotFound, IsEmpty {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<E> v = vertices.get(i);
            if (v.getData().equals(data)) return v;
        }
        throw new ItemNotFound("Vértice no encontrado: " + data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices.size(); i++) {
            try {
                sb.append(vertices.get(i).toString()).append("\n");
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public LinkedList<E> bfs(E start) throws ItemNotFound, IsEmpty {
        LinkedList<E> result = new LinkedList<>();
        LinkedList<Vertex<E>> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices.size()];

        Vertex<E> startVertex = findVertex(start);
        int idxStart = vertices.indexOf(startVertex);
        visited[idxStart] = true;
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            Vertex<E> current = queue.get(0);
            queue.remove(0);
            result.add(current.getData());

            LinkedList<Edge<E>> adj = current.getAdjList();
            for (int i = 0; i < adj.size(); i++) {
                Vertex<E> neighbor = adj.get(i).getDestination();
                int idx = vertices.indexOf(neighbor);
                if (!visited[idx]) {
                    visited[idx] = true;
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

    public LinkedList<E> dfs(E start) throws ItemNotFound, IsEmpty {
        LinkedList<E> result = new LinkedList<>();
        boolean[] visited = new boolean[vertices.size()];
        Vertex<E> startVertex = findVertex(start);
        dfsRecursive(startVertex, visited, result);
        return result;
    }

    private void dfsRecursive(Vertex<E> v, boolean[] visited, LinkedList<E> result) {
        int idx = vertices.indexOf(v);
        if (visited[idx]) return;
        visited[idx] = true;
        result.add(v.getData());

        LinkedList<Edge<E>> adj = v.getAdjList();
        for (int i = 0; i < adj.size(); i++) {
            try {
                dfsRecursive(adj.get(i).getDestination(), visited, result);
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();
            }

        }
    }

    public boolean hasCycle() {
    boolean[] visited = new boolean[vertices.size()];
    for (int i = 0; i < vertices.size(); i++) {
        try {
            if (!visited[i]) {
                if (hasCycleDFS(vertices.get(i), visited, null)) return true;
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
    }
    return false;
}

    private boolean hasCycleDFS(Vertex<E> current, boolean[] visited, Vertex<E> parent) {
        int idx = vertices.indexOf(current);
        visited[idx] = true;
        LinkedList<Edge<E>> adj = current.getAdjList();
        for (int i = 0; i < adj.size(); i++) {
            Vertex<E> neighbor = null;
            try {
                neighbor = adj.get(i).getDestination();
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();
            }

            int nIdx = vertices.indexOf(neighbor);
            if (!visited[nIdx]) {
                if (hasCycleDFS(neighbor, visited, current)) return true;
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        return false;
    }

public int countConnectedComponents() {//try catch para el itemnofound p
    boolean[] visited = new boolean[vertices.size()];
    int count = 0;
    for (int i = 0; i < vertices.size(); i++) {
        try {
            if (!visited[i]) {
                LinkedList<E> temp = new LinkedList<>();
                dfsRecursive(vertices.get(i), visited, temp);
                count++;
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
    }
    return count;
}


    public LinkedList<E> getIsolatedZones() {
        LinkedList<E> isolated = new LinkedList<>();
        for (int i = 0; i < vertices.size(); i++) {
            try {
                if (vertices.get(i).getAdjList().isEmpty()) {
                    isolated.add(vertices.get(i).getData());
            }
        } catch (IsEmpty | ItemNotFound e) {
            e.printStackTrace();
        }
        }
        return isolated;
    }
}
