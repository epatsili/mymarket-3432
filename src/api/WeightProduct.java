package api;

/**
 * Represents a product measured in weight (kilograms).
 */
public class WeightProduct extends Product {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private double availableWeight; // In kilograms

    /**
     * Constructor for creating a weight-based product.
     *
     * @param title           the title of the product
     * @param description     the description of the product
     * @param category        the category of the product
     * @param subcategory     the subcategory of the product
     * @param price           the price of the product per kilogram
     * @param availableWeight the available weight of the product in kilograms
     * @throws IllegalArgumentException if availableWeight is negative
     */
    public WeightProduct(String title, String description, String category, String subcategory, double price, double availableWeight) {
        super(title, description, category, subcategory, price);
        if (availableWeight < 0) {
            throw new IllegalArgumentException("Available weight cannot be negative.");
        }
        this.availableWeight = availableWeight;
    }

    /**
     * Gets the available weight of the product.
     *
     * @return the available weight in kilograms
     */
    public double getAvailableWeight() {
        return availableWeight;
    }

    /**
     * Sets the available weight of the product.
     *
     * @param availableWeight the new available weight
     * @throws IllegalArgumentException if availableWeight is negative
     */
    public void setAvailableWeight(double availableWeight) {
        if (availableWeight < 0) {
            throw new IllegalArgumentException("Available weight cannot be negative.");
        }
        this.availableWeight = availableWeight;
    }

    /**
     * Validates the product details.
     *
     * @return true if the price is positive and available weight is non-negative
     */
    @Override
    public boolean validate() {
        return getPrice() > 0 && availableWeight >= 0;
    }

    /**
     * Reduces the stock of the product by a specified weight.
     *
     * @param weight the weight to reduce in kilograms
     * @throws IllegalArgumentException if the weight is invalid or exceeds available stock
     */
    @Override
    public void reduceStock(double weight) {
        if (!validatePurchase(weight)) {
            throw new IllegalArgumentException("Invalid weight: Cannot reduce stock by " + String.format("%.2f", weight) + " kg.");
        }
        this.availableWeight -= weight;
    }

    /**
     * Validates if a purchase of a specific weight can be made.
     *
     * @param weight the weight to validate
     * @return true if the weight is valid and does not exceed available stock
     */
    @Override
    public boolean validatePurchase(double weight) {
        if (weight <= 0) {
            return false;
        }
        return weight <= availableWeight;
    }

    /**
     * Provides a string representation of the product.
     *
     * @return a string containing product details and available weight
     */
    @Override
    public String toString() {
        return super.toString() + ", Available Weight: " + String.format("%.2f", availableWeight) + " kg";
    }
}
