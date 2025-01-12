package api;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for searching products based on various criteria.
 */
public class ProductSearch {

    /**
     * Searches for products by title and/or category and/or subcategory.
     *
     * @param products    List of all available products (cannot be null).
     * @param title       Search text for product title (can be null or empty).
     * @param category    Category name (can be null or empty).
     * @param subcategory Subcategory name (can be null or empty).
     * @return Filtered list of products matching the criteria.
     * @throws IllegalArgumentException if the products list is null.
     */
    public static List<Product> searchProducts(List<Product> products, String title, String category, String subcategory) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null.");
        }

        // Normalize inputs
        title = title != null ? title.trim().toLowerCase() : null;
        category = category != null ? category.trim() : null;
        subcategory = subcategory != null ? subcategory.trim() : null;

        // Perform filtering
        String finalSubcategory = subcategory;
        String finalCategory = category;
        String finalTitle = title;
        return products.stream()
                .filter(product -> matches(finalTitle, product.getTitle().toLowerCase()))
                .filter(product -> matches(finalCategory, product.getCategory()))
                .filter(product -> matches(finalSubcategory, product.getSubcategory()))
                .collect(Collectors.toList());
    }

    /**
     * Normalizes input strings by trimming and converting to lowercase.
     *
     * @param input The input string to normalize.
     * @return Normalized string or null if input is null or empty.
     */
    private static String normalizeInput(String input) {
        return (input != null && !input.trim().isEmpty()) ? input.trim().toLowerCase() : null;
    }

    /**
     * Helper method to check if a value matches a criteria.
     *
     * @param criteria The search criteria (null or empty matches all).
     * @param value    The actual value to check against.
     * @return True if the criteria is null/empty or matches the value.
     */
    private static boolean matches(String criteria, String value) {
        if (criteria == null || criteria.isEmpty()) return true;
        return value != null && value.trim().equalsIgnoreCase(criteria.trim());
    }
}
