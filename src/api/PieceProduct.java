package api;

/**
 * Represents a product measured in discrete pieces.
 */
public class PieceProduct extends Product {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private int availablePieces;

    /**
     * Constructor for creating a piece-based product.
     *
     * @param title           the title of the product
     * @param description     the description of the product
     * @param category        the category of the product
     * @param subcategory     the subcategory of the product
     * @param price           the price of the product
     * @param availablePieces the number of available pieces
     */
    public PieceProduct(String title, String description, String category, String subcategory, double price, int availablePieces) {
        super(title, description, category, subcategory, price);
        if (availablePieces < 0) {
            throw new IllegalArgumentException("Available pieces cannot be negative.");
        }
        this.availablePieces = availablePieces;
    }

    /**
     * Gets the number of available pieces.
     *
     * @return the number of available pieces
     */
    public int getAvailablePieces() {
        return availablePieces;
    }

    /**
     * Sets the number of available pieces.
     *
     * @param availablePieces the new available pieces
     * @throws IllegalArgumentException if the available pieces are negative
     */
    public void setAvailablePieces(int availablePieces) {
        if (availablePieces < 0) {
            throw new IllegalArgumentException("Available pieces cannot be negative.");
        }
        this.availablePieces = availablePieces;
    }

    /**
     * Validates the product details.
     *
     * @return true if the price is positive and available pieces are non-negative
     */
    @Override
    public boolean validate() {
        return getPrice() > 0 && availablePieces >= 0;
    }

    /**
     * Reduces the stock of the product by a specified quantity.
     *
     * @param quantity the quantity to reduce
     * @throws IllegalArgumentException if the quantity is invalid or exceeds available stock
     */
    @Override
    public void reduceStock(double quantity) {
        if (!validatePurchase(quantity)) {
            throw new IllegalArgumentException("Invalid quantity: Cannot reduce stock by " + (int) quantity + " pieces.");
        }
        this.availablePieces -= (int) quantity;
    }

    /**
     * Validates if a purchase of a specific quantity can be made.
     *
     * @param quantity the quantity to validate
     * @return true if the quantity is valid and does not exceed available stock
     */
    @Override
    public boolean validatePurchase(double quantity) {
        if (quantity <= 0 || quantity != Math.floor(quantity)) { // Check for positive integer quantity
            return false;
        }
        return (int) quantity <= availablePieces;
    }

    /**
     * Provides a string representation of the product.
     *
     * @return a string containing product details and available pieces
     */
    @Override
    public String toString() {
        return super.toString() + ", Available Pieces: " + availablePieces;
    }
}
