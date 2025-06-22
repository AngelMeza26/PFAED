package tree;

import exceptions.*;
import list.ArrayList;

/**
 * BNodePlus genérico para un B+ Tree de orden 'n'.
 * @param <E> tipo de clave, debe ser Comparable
 */
public class BNodePlus<E extends Comparable<E>> {
    private static int nextId = 0;
    private final int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNodePlus<E>> children;
    protected int count;  // número actual de claves válidas
    protected boolean leaf;

    /**
     * Construye un nodo B+ de capacidad máxima 'n' claves y 'n+1' hijos.
     * @param n orden del árbol (número máximo de claves)
     * @throws ItemDuplicated 
     */
    public BNodePlus(int n) throws ItemDuplicated {
        this.idNode = nextId++;
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
        this.count = 0;
        this.leaf = true;
        // Inicializa con nulls para posiciones fijas
        for (int i = 0; i < n; i++) {
            keys.add(null);
        }
        for (int i = 0; i < n + 1; i++) {
            children.add(null);
        }
    }

    /**
     * Obtiene el ID único de este nodo.
     */
    public int getIdNode() {
        return idNode;
    }

    /**
     * Indica si el nodo está lleno (tiene n claves).
     * @param n capacidad máxima de claves
     */
    public boolean nodeFull(int n) {
        return count == n;
    }

    /**
     * Indica si el nodo está por debajo de la mitad (menos de ceil(n/2)).
     * @param n orden del árbol
     */
    public boolean nodeUnderflow(int n) {
        int min = (int) Math.ceil((n + 1) / 2.0) - 1;
        return count < min;
    }

    /**
     * Busca clave x en este nodo; si existe, pos[0] será su índice y retorna true;
     * sino, pos[0] marca el hijo donde descender y retorna false.
     * @throws IsEmpty 
     * @throws ItemNotFound 
     */
    public boolean searchNode(E x, int[] pos) throws IsEmpty, ItemNotFound {
        int i = 0;
        while (i < count && keys.get(i).compareTo(x) < 0) {
            i++;
        }
        pos[0] = i;
        if (i < count && keys.get(i).compareTo(x) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Node" + idNode + " keys:(");
        for (int i = 0; i < count; i++) {
            try {
				sb.append(keys.get(i));
			} catch (IsEmpty e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ItemNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (i < count - 1) sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Agrega clave al final (asume espacio disponible).
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void addKey(E x) throws ItemNotFound, IsEmpty {
        keys.set(count, x);
        count++;
    }

    /**
     * Inserta clave en posición pos y desplaza las posteriores.
     * @throws IsEmpty 
     * @throws ItemNotFound 
     */
    public void insertKey(E x, int pos) throws ItemNotFound, IsEmpty {
        for (int i = count - 1; i >= pos; i--) {
            keys.set(i + 1, keys.get(i));
        }
        keys.set(pos, x);
        count++;
    }

    /**
     * Elimina clave en posición pos y desplaza las posteriores hacia la izquierda.
     * @throws IsEmpty 
     * @throws ItemNotFound 
     */
    public void removeKey(int pos) throws ItemNotFound, IsEmpty {
        for (int i = pos; i < count - 1; i++) {
            keys.set(i, keys.get(i + 1));
        }
        keys.set(count - 1, null);
        count--;
    }

    /**
     * Establece el hijo en posición i.
     * @throws ItemNotFound 
     * @throws IsEmpty 
     */
    public void setChild(int i, BNodePlus<E> child) throws ItemNotFound, IsEmpty {
        children.set(i, child);
        if (child != null) {
            child.leaf = false;
        }
    }

    /**
     * Obtiene el hijo en posición i.
     * @throws IsEmpty 
     * @throws ItemNotFound 
     */
    public BNodePlus<E> getChild(int i) throws IsEmpty, ItemNotFound {
        return children.get(i);
    }
}
	