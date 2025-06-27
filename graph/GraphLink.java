
package graph;

import exceptions.ItemNotFound;
import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import list.LinkedList;
import graph.Vertex;
import graph.Edge;

//Grafo genérico ponderado para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
// Incluye rutas óptimas (Dijkstra), BFS, DFS, detección de ciclos, componentes conexas y zonas aisladas.
 // @param <E> tipo de datos de los vértices (Comparable)

//declaramos
public class GraphLink<E extends Comparable<E>> {
    private final LinkedList<Vertex<E>> vertices;//lista enlazA de vertices

    public GraphLink() {
        vertices = new LinkedList<>();
    }
//AÑADIMOS UN VERTICE A LA LISTA
    public void addVertex(E data) throws ItemDuplicated {
        Vertex<E> v = new Vertex<>(data);//inicializamos una lista vacia donde aladiremos vertices
        if (vertices.indexOf(v) >= 0) {//el vertice ya esta en la lista?
            throw new ItemDuplicated("Vértice ya existe: " + data);
        }
        vertices.add(v);//añade
    }
//ELIMINAMOOOOS VERTICES
    public void removeVertex(E data) throws IsEmpty, ItemNotFound {
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío");//LISTA VACIA??
        Vertex<E> v = findVertex(data);//si no lo encuntra, itemnotfound, peor sisi devuelve v
        for (int i = 0; i < vertices.size(); i++) {
            vertices.get(i).removeEdgeTo(v);//eliminamos aristas
        }
        vertices.remove(v);
    }
//AÑDE UNA ARISTA
    //NECESITAMOS ORIGEN/ DESTINO Y PESO
    public void addEdge(E src, E dest, double weight) throws ItemNotFound, ItemDuplicated, IsEmpty {
        Vertex<E> vSrc = findVertex(src);
        Vertex<E> vDest = findVertex(dest);
        Edge<E> edge = new Edge<>(vDest, weight);//CREA objeto edge con el destino y peso
        vSrc.addEdge(edge);
    }
//ELIMINAMOS ARISRA

    //lo miso que el anterior pero sin peso
    public void removeEdge(E src, E dest) throws ItemNotFound, IsEmpty {
        Vertex<E> vSrc = findVertex(src);
        Vertex<E> vDest = findVertex(dest);
        vSrc.removeEdgeTo(vDest);//Pide al vértice origen que quite de su lista de adyacencia cualquier arista que apunte a vDest.
    }

    //DIJKSTRA, ruta mas corta 
    public LinkedList<E> shortestPath(E origin, E destination) throws ItemNotFound, IsEmpty, ItemDuplicated {
        if (vertices.isEmpty()) throw new IsEmpty("El grafo está vacío"); //esta vacio?
        Vertex<E> src = findVertex(origin);
        Vertex<E> dst = findVertex(destination);
        int n = vertices.size();//n sera el total de vertices en el grafo
        double[] dist = new double[n];// la distancia mínima que hemos hallado hasta él desde src
        Vertex<E>[] prev = new Vertex[n];//nuestras migajas de risitos de oro
        boolean[] visited = new boolean[n];//marco lo que ya recorrimos

        //comenzamos la ruta en 0
        for (int i = 0; i < n; i++) {
            dist[i] = vertices.get(i).equals(src) ? 0.0 : Double.POSITIVE_INFINITY;//inicializamos la distacia en 0
            prev[i] = null;
            visited[i] = false;
        }
        
        for (int k = 0; k < n; k++) {
            int u = -1;//guarda la ruta que aun no iniciamos 
            double min = Double.POSITIVE_INFINITY;//Esto te permite llevar la “distancia mínima encontrada hasta ahora” de forma segura.
         //BUSCAA el vértice no visitado con menor distancia
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }
            if (u < 0) break;//si no hay mas vertices procesados, salimos
         
            //Si llegamos al destino, terminamos antes:
            visited[u] = true;
            Vertex<E> vU = vertices.get(u);         
            if (vU.equals(dst)) break;// si el valor de u es el destio terminamos anticipadamente

         //actualizmos las distancias
            LinkedList<Edge<E>> adj = vU.getAdjList();//obtiene las lista de atista de vu
            //recorremos
            for (int j = 0; j < adj.size(); j++) {
                Edge<E> e = adj.get(j);
                Vertex<E> vV = e.getDestination();
                int vIdx = vertices.indexOf(vV);
                //Si por alguna razón no encontramos el índice, lo saltamos
             //por siacaso jajajaja
                if (vIdx < 0) continue;
                //Calcular nueva distancia alternativa
                double alt = dist[u] + e.getWeight();
                //Si este camino mejora la distancia conocida, actualizar
                if (alt < dist[vIdx]) {
                    dist[vIdx] = alt;
                    prev[vIdx] = vU;
                }
            }
        }
//Reconstrucción del camino
        LinkedList<E> path = new LinkedList<>();//nuestra mochila que guarda los datos E desde origen hasta destino
        Vertex<E> step = dst;//corredor de vertices
        while (step != null) {
            path.add(0, step.getData());//Insertar el dato al principio de la lista
            int idx = vertices.indexOf(step);//Buscar el índice del vértice actual
            step = (idx >= 0) ? prev[idx] : null;//actualiza 
        }
        return path;//devuelve el camino
    }
/////////////////////////////////////////////////////////////
    //encontramos el verticeeeeee 
    private Vertex<E> findVertex(E data) throws ItemNotFound, IsEmpty {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex<E> v = vertices.get(i);//asuminmos el valor del corredor con el que visita
            if (v.getData().equals(data)) return v;
        }
        throw new ItemNotFound("Vértice no encontrado: " + data);
    }
///////////////////////////
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices.size(); i++) {
            try {
                sb.append(vertices.get(i).toString()).append("\n");
            } catch (IsEmpty | ItemNotFound e) {
                e.printStackTrace();//imprime el error en consola pero no lo abortaea
            }
        }
        return sb.toString();//retorna el string builder
    }
////////////////////////////////////////////////////////////////
    //BFS
    
    public LinkedList<E> bfs(E start) throws ItemNotFound, IsEmpty {
        LinkedList<E> result = new LinkedList<>();//almacena el recorrido final
        LinkedList<Vertex<E>> queue = new LinkedList<>();//cola de vericews por visitar
        boolean[] visited = new boolean[vertices.size()];//indica si ya se visitó el vértice en la posición i.

        Vertex<E> startVertex = findVertex(start);
        int idxStart = vertices.indexOf(startVertex);
        visited[idxStart] = true;
        queue.add(startVertex);

        while (!queue.isEmpty()) {
            Vertex<E> current = queue.get(0);
            queue.remove(0);//eliminamos de la cola
            result.add(current.getData());//agregamos a la cola
// Explora vecinos del nodo actual
            LinkedList<Edge<E>> adj = current.getAdjList();
            for (int i = 0; i < adj.size(); i++) {//bistamos a los vvecimnos
                Vertex<E> neighbor = adj.get(i).getDestination();//Extraemos el vértice destino
                int idx = vertices.indexOf(neighbor);//Buscamos el índice del neighbor
                if (!visited[idx]) {
                    visited[idx] = true;
                    queue.add(neighbor);
                }
            }
        }
        return result;
    }

 
 ////////////////////////////////////
//DFS
 
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
