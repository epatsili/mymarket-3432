package api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Represents an order placed by a customer.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private final String orderId;
    private final Map<Product, Double> orderedProducts; // Support both pieces and weight
    private final LocalDateTime orderDate;

    /**
     * Constructor for creating an order.
     *
     * @param orderedProducts a map of products to their quantities
     * @throws IllegalArgumentException if the ordered products map is null, empty, or contains invalid quantities
     */
    public Order(Map<Product, Double> orderedProducts) {
        if (orderedProducts == null || orderedProducts.isEmpty()) {
            throw new IllegalArgumentException("Ordered products cannot be null or empty.");
        }
        if (orderedProducts.values().stream().anyMatch(quantity -> quantity <= 0)) {
            throw new IllegalArgumentException("Quantities must be greater than zero.");
        }

        this.orderId = UUID.randomUUID().toString();
        this.orderedProducts = Map.copyOf(orderedProducts); // Ensure immutability
        this.orderDate = LocalDateTime.now(); // Sets the order date to the current date-time
    }

    /**
     * Gets the map of ordered products and their quantities.
     *
     * @return the map of ordered products
     */
    public Map<Product, Double> getOrderedProducts() {
        return orderedProducts;
    }

    /**
     * Gets the date of the order.
     *
     * @return the order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Gets the unique ID of the order.
     *
     * @return the order ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Calculates the total cost of the order.
     *
     * @return the total cost of the order
     */
    public double getTotalCost() {
        return orderedProducts.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    /**
     * Provides a string representation of the order.
     *
     * @return a string containing order details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderId).append("\n");
        sb.append("Order Date: ").append(orderDate).append("\n");
        sb.append("Ordered Products:\n");
        orderedProducts.forEach((product, quantity) -> sb.append("- ")
                .append(product.getTitle())
                .append(": ")
                .append(quantity)
                .append(product instanceof PieceProduct ? " pieces" : " kg")
                .append(" (Total: €")
                .append(String.format("%.2f", product.getPrice() * quantity))
                .append(")\n"));
        sb.append("Total Cost: €").append(String.format("%.2f", getTotalCost()));
        return sb.toString();
    }
}
