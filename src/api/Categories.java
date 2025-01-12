package api;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing categories and subcategories.
 */
public class Categories implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private static final Map<String, String[]> categories = new HashMap<>();

    /**
     * Loads categories and subcategories from the specified file.
     * The file should have lines in the format: Category (Subcategory1@Subcategory2...).
     *
     * @param filePath the path to the file containing categories and subcategories
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if the file contains invalid lines
     */
    public static void loadCategories(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            categories.clear(); // Clear existing categories before loading
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split("\\s*\\(\\s*");
                if (parts.length != 2 || !parts[1].endsWith(")")) {
                    throw new IllegalArgumentException("Invalid format in categories file: " + line);
                }
                String category = parts[0].trim();
                String[] subcategories = parts[1].replace(")", "").split("@");
                categories.put(category, subcategories);
            }
        }
    }

    /**
     * Saves the current categories to a human-readable file for persistence.
     *
     * @param filePath the path to the file where categories will be saved
     * @throws IOException if an I/O error occurs
     */
    public static void saveCategories(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String[]> entry : categories.entrySet()) {
                writer.write(entry.getKey() + " (" + String.join("@", entry.getValue()) + ")");
                writer.newLine();
            }
        }
    }

    /**
     * Gets the map of categories and their subcategories.
     *
     * @return an unmodifiable map of categories to subcategories
     */
    public static Map<String, String[]> getCategories() {
        return Collections.unmodifiableMap(categories);
    }

    /**
     * Checks if a category exists.
     *
     * @param category the category to check
     * @return true if the category exists, false otherwise
     * @throws IllegalArgumentException if the category is null or empty
     */
    public static boolean categoryExists(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        return categories.containsKey(category);
    }

    /**
     * Checks if a subcategory exists under a specified category.
     *
     * @param category    the category to check
     * @param subcategory the subcategory to check
     * @return true if the subcategory exists under the category, false otherwise
     * @throws IllegalArgumentException if the category or subcategory is null or empty
     */
    public static boolean subcategoryExists(String category, String subcategory) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (subcategory == null || subcategory.trim().isEmpty()) {
            throw new IllegalArgumentException("Subcategory cannot be null or empty.");
        }
        if (!categories.containsKey(category)) {
            return false;
        }
        for (String sub : categories.get(category)) {
            if (sub.equalsIgnoreCase(subcategory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all loaded categories.
     */
    public static void clearCategories() {
        categories.clear();
    }

    /**
     * Adds a new category with its subcategories.
     *
     * @param category      the category to add
     * @param subcategories the subcategories to associate with the category
     * @throws IllegalArgumentException if the category or subcategories are invalid
     */
    public static void addCategory(String category, String[] subcategories) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        if (subcategories == null || subcategories.length == 0) {
            throw new IllegalArgumentException("Subcategories cannot be null or empty.");
        }
        categories.put(category, subcategories);
    }

    /**
     * Removes a category.
     *
     * @param category the category to remove
     * @throws IllegalArgumentException if the category is null or empty
     */
    public static void removeCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        categories.remove(category);
    }
}
