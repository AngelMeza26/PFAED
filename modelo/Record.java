package modelo;

import exceptions.IsEmpty;
import exceptions.ItemDuplicated;
import exceptions.ItemNotFound;
import list.ArrayList;

/**
 * Registro de ítem para el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Representa un producto con su código, nombre, cantidad en stock, ubicación y categorías.
 */
public class Record implements Comparable<Record> {
    private String code;
    private String name;
    private int quantity;
    private String location;
    private ArrayList<String> categories;

    /**
     * Crea un registro de ítem con categorías vacías.
     * @param code código único del ítem
     * @param name nombre descriptivo
     * @param quantity cantidad inicial en stock
     * @param location ubicación (p.ej. pasillo o zona)
     */
    public Record(String code, String name, int quantity, String location) {
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
        this.categories = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Establece nueva ubicación para el ítem.
     * @param location ubicación actualizada
     */
    public void setLocation(String location) {
        if (location != null && !location.isEmpty()) {
            this.location = location;
        }
    }

    /**
     * Incrementa stock en la cantidad indicada.
     * @param amount unidades a agregar
     */
    public void addStock(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    /**
     * Reduce stock en la cantidad indicada, sin permitir stock negativo.
     * @param amount unidades a retirar
     */
    public void removeStock(int amount) {
        if (amount > 0 && amount <= this.quantity) {
            this.quantity -= amount;
        }
    }

    /**
     * Añade una categoría al ítem si no existe.
     * @param category nombre de categoría
     * @throws ItemDuplicated 
     */
    public void addCategory(String category) throws ItemDuplicated {
        if (category != null && !category.isEmpty()) {
            int idx = categories.indexOf(category);
            if (idx < 0) {
                categories.add(category);
            }
        }
    }

    /**
     * Elimina una categoría existente.
     * @param category nombre de categoría a eliminar
     * @throws IsEmpty si no hay categorías
     * @throws ItemNotFound si la categoría no existe
     */
    public void removeCategory(String category) throws IsEmpty, ItemNotFound {
        if (categories.isEmpty()) {
            throw new IsEmpty("No hay categorías para eliminar");
        }
        int idx = categories.indexOf(category);
        if (idx < 0) {
            throw new ItemNotFound("Categoría no encontrada: " + category);
        }
        categories.remove(idx);
    }

    /**
     * Devuelve lista de categorías asignadas.
     * @return lista de categorías
     */
    public ArrayList<String> getCategories() {
        return categories;
    }

    @Override
    public int compareTo(Record other) {
        return this.code.compareTo(other.code);
    }

    @Override
    public String toString() {
        return "Record{" +
               "code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", quantity=" + quantity +
               ", location='" + location + '\'' +
               ", categories=" + categories +
               '}';
    }
}
