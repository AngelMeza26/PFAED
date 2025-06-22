package modelo;

/**
 * Representa un ítem en el Sistema de Gestión y Optimización de Inventarios en Almacenes.
 * Incluye código único, nombre descriptivo, cantidad en stock y ubicación en almacén.
 */
public class Item implements Comparable<Item> {
    private String code;
    private String name;
    private int quantity;
    private String location;

    /**
     * Crea un ítem con los datos básicos.
     * @param code código único (no nulo)
     * @param name nombre descriptivo
     * @param quantity cantidad inicial en stock (>=0)
     * @param location ubicación en almacén (e.g., zona-pasillo)
     */
    public Item(String code, String name, int quantity, String location) {
        if (code == null || code.isEmpty()) throw new IllegalArgumentException("Código no puede ser nulo o vacío");
        if (quantity < 0) throw new IllegalArgumentException("Cantidad no puede ser negativa");
        this.code = code;
        this.name = name;
        this.quantity = quantity;
        this.location = location;
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
     * Actualiza la ubicación del ítem.
     * @param location nueva ubicación
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Aumenta la cantidad de stock.
     * @param amount unidades a agregar (positivas)
     */
    public void addStock(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    /**
     * Reduce la cantidad de stock sin caer en negativo.
     * @param amount unidades a retirar (positivas)
     */
    public void removeStock(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
        }
    }

    @Override
    public int compareTo(Item other) {
        return this.code.compareTo(other.code);
    }

    @Override
    public String toString() {
        return "Item{" +
               "code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", quantity=" + quantity +
               ", location='" + location + '\'' +
               '}';
    }
}
