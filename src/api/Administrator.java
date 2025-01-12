package api;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an administrator in the supermarket system.
 */
public class Administrator extends User implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility

    /**
     * Constructor for creating an administrator.
     *
     * @param username the username of the administrator
     * @param password the password of the administrator
     */
    public Administrator(String username, String password) {
        super(username, password);
    }

    /**
     * Adds a new product to the list of products.
     *
     * @param products   the list of products
     * @param newProduct the product to add
     * @throws IllegalArgumentException if the new product is null
     */
    public void addProduct(List<Product> products, Product newProduct) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null.");
        }
        if (newProduct == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (products.contains(newProduct)) {
            throw new IllegalArgumentException("Product already exists in the list.");
        }
        products.add(newProduct);
    }

    /**
     * Edits an existing product's details.
     *
     * @param product     the product to edit
     * @param title       the new title of the product
     * @param description the new description of the product
     * @param price       the new price of the product
     * @param quantity    the new quantity of the product
     * @throws IllegalArgumentException if any input is invalid
     */
    public void editProduct(Product product, String title, String description, double price, double quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);

        if (product instanceof PieceProduct) {
            ((PieceProduct) product).setAvailablePieces((int) quantity);
        } else if (product instanceof WeightProduct) {
            ((WeightProduct) product).setAvailableWeight(quantity);
        } else {
            throw new IllegalArgumentException("Unsupported product type.");
        }
    }

    /**
     * Removes a product from the list of products.
     *
     * @param products the list of products
     * @param product  the product to remove
     * @throws IllegalArgumentException if the product or product list is null
     */
    public void removeProduct(List<Product> products, Product product) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null.");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (!products.contains(product)) {
            throw new IllegalArgumentException("Product does not exist in the list.");
        }
        products.remove(product);
    }

    /**
     * Returns the role of the user.
     *
     * @return "Administrator"
     */
    @Override
    public String getRole() {
        return "Administrator";
    }
}
