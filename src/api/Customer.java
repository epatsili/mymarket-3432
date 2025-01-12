package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the supermarket system.
 */
public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private final List<Order> orderHistory;
    private final Cart cart;

    /**
     * Constructor for creating a customer.
     *
     * @param username the username of the customer
     * @param password the password of the customer
     */
    public Customer(String username, String password) {
        super(username, password);
        this.orderHistory = new ArrayList<>();
        this.cart = new Cart();
    }

    /**
     * Gets the order history of the customer.
     *
     * @return a list of orders placed by the customer
     */
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory); // Return a copy to maintain encapsulation
    }

    /**
     * Gets the shopping cart of the customer.
     *
     * @return the customer's shopping cart
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Adds a product to the customer's cart.
     *
     * @param product  the product to add
     * @param quantity the quantity to add
     * @throws IllegalArgumentException if the product is null or the quantity is invalid
     */
    public void addToCart(Product product, double quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        cart.addProduct(product, quantity);
    }

    /**
     * Updates the quantity of a product in the cart.
     *
     * @param product  the product to update
     * @param quantity the new quantity
     * @throws IllegalArgumentException if the product is not in the cart or the quantity is invalid
     */
    public void updateCart(Product product, double quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        cart.updateProductQuantity(product, quantity);
    }

    /**
     * Removes a product from the customer's cart.
     *
     * @param product the product to remove
     * @throws IllegalArgumentException if the product is not in the cart
     */
    public void removeFromCart(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        cart.removeProduct(product);
    }

    /**
     * Completes the current order, moving it to the order history and clearing the cart.
     *
     * @throws IllegalStateException if the cart is empty
     */
    public void completeOrder() {
        if (cart.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot complete order: The cart is empty.");
        }

        Order newOrder = new Order(cart.getProducts());
        orderHistory.add(newOrder);
        cart.clearCart();
    }

    /**
     * Provides a string representation of the customer.
     *
     * @return a string containing customer details and their order history
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer Username: ").append(getUsername()).append("\n");
        sb.append("Order History:\n");

        if (orderHistory.isEmpty()) {
            sb.append("No orders placed yet.\n");
        } else {
            for (int i = 0; i < orderHistory.size(); i++) {
                sb.append("Order ").append(i + 1).append(":\n");
                sb.append(orderHistory.get(i)).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Returns the role of the user.
     *
     * @return "Customer"
     */
    @Override
    public String getRole() {
        return "Customer";
    }
}
