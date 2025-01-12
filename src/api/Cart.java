package api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a shopping cart for a customer.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private final Map<Product, Double> products; // Use Double for quantities to handle both pieces and weight

    /**
     * Constructor for creating an empty cart.
     */
    public Cart() {
        this.products = new HashMap<>();
    }

    /**
     * Adds a product to the cart with the specified quantity.
     * If the product is already in the cart, the quantity is incremented.
     *
     * @param product  the product to add
     * @param quantity the quantity of the product
     * @throws IllegalArgumentException if the quantity is less than or equal to zero or exceeds stock
     */
    public void addProduct(Product product, double quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        validateStock(product, quantity);
        products.put(product, products.getOrDefault(product, 0.0) + quantity);
    }

    /**
     * Updates the quantity of a product in the cart.
     * If the quantity is zero, the product is removed from the cart.
     *
     * @param product  the product to update
     * @param quantity the new quantity
     * @throws IllegalArgumentException if the quantity is negative or exceeds stock
     */
    public void updateProductQuantity(Product product, double quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (quantity == 0) {
            products.remove(product);
        } else {
            validateStock(product, quantity);
            products.put(product, quantity);
        }
    }

    /**
     * Removes a product from the cart.
     *
     * @param product the product to remove
     * @throws IllegalArgumentException if the product is null
     */
    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        products.remove(product);
    }

    /**
     * Gets the products in the cart along with their quantities.
     *
     * @return a map of products to their quantities
     */
    public Map<Product, Double> getProducts() {
        return new HashMap<>(products); // Return a copy to maintain encapsulation
    }

    /**
     * Calculates the total cost of all products in the cart.
     *
     * @return the total cost of the cart
     */
    public double getTotalCost() {
        return products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    /**
     * Clears all products from the cart.
     */
    public void clearCart() {
        products.clear();
    }

    /**
     * Provides a string representation of the cart's contents.
     *
     * @return a string containing cart details
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shopping Cart:\n");

        if (products.isEmpty()) {
            sb.append("The cart is empty.\n");
        } else {
            products.forEach((product, quantity) -> sb.append("- ")
                    .append(product.getTitle())
                    .append(": ")
                    .append(String.format("%.2f", quantity))
                    .append(product instanceof PieceProduct ? " pieces" : " kg")
                    .append(" (Total: €")
                    .append(String.format("%.2f", product.getPrice() * quantity))
                    .append(")\n"));
            sb.append("Total Cost: €").append(String.format("%.2f", getTotalCost()));
        }

        return sb.toString();
    }

    /**
     * Validates the stock availability for a product and quantity.
     *
     * @param product  the product to check
     * @param quantity the quantity to validate
     * @throws IllegalArgumentException if the quantity exceeds stock or is invalid
     */
    private void validateStock(Product product, double quantity) {
        if (product instanceof PieceProduct) {
            if (quantity > ((PieceProduct) product).getAvailablePieces()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock for " + product.getTitle() + ".");
            }
        } else if (product instanceof WeightProduct) {
            if (quantity > ((WeightProduct) product).getAvailableWeight()) {
                throw new IllegalArgumentException("Requested weight exceeds available stock for " + product.getTitle() + ".");
            }
        } else {
            throw new IllegalArgumentException("Unknown product type.");
        }
    }
}
