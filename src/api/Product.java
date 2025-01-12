package api;

import java.io.Serializable;

/**
 * Abstract class representing a product in the supermarket.
 */
public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private String title;
    private String description;
    private String category;
    private String subcategory;
    private double price;

    /**
     * Constructor for creating a product.
     *
     * @param title       the title of the product
     * @param description the description of the product
     * @param category    the category of the product
     * @param subcategory the subcategory of the product
     * @param price       the price of the product
     */
    public Product(String title, String description, String category, String subcategory, double price) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (subcategory == null || subcategory.trim().isEmpty()) {
            throw new IllegalArgumentException("Subcategory cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        this.title = title;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.price = price;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        this.description = description;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    /**
     * Reduces the stock of the product.
     *
     * @param amount the amount to reduce
     * @throws IllegalArgumentException if the amount is invalid or exceeds available stock
     */
    public abstract void reduceStock(double amount);

    /**
     * Validates if a purchase of a specific amount can be made.
     *
     * @param amount the amount to validate
     * @return true if the purchase is valid, false otherwise
     */
    public abstract boolean validatePurchase(double amount);

    /**
     * Abstract method to validate the product details.
     *
     * @return true if the product details are valid, false otherwise
     */
    public abstract boolean validate();

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", price=" + String.format("%.2f", price) +
                '}';
    }
}
