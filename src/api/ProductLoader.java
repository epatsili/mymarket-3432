package api;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for loading and saving products to/from files.
 */
public class ProductLoader {

    /**
     * Reads products from a file and returns a list of products.
     * The file format must follow the specified structure:
     * Title: [title]
     * Description: [description]
     * Category: [category]
     * Subcategory: [subcategory]
     * Price: €[price]
     * Quantity: [quantity] (e.g., "200 κιλά" or "50 τεμάχια")
     *
     * @param filePath the path to the file containing product data
     * @return a list of loaded products
     * @throws IOException if an I/O error occurs
     */
    public static List<Product> loadProducts(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        List<Product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Skip empty or malformed lines
                    if (!line.startsWith("Title:")) {
                        continue;
                    }

                    // Parse product attributes
                    String title = line.split(": ", 2)[1].trim();
                    String description = reader.readLine().split(": ", 2)[1].trim();
                    String category = reader.readLine().split(": ", 2)[1].trim();
                    String subcategory = reader.readLine().split(": ", 2)[1].trim();
                    String priceText = reader.readLine().split(": ", 2)[1].replace("€", "").trim().replace(",", ".");
                    double price = Double.parseDouble(priceText);
                    String quantityLine = reader.readLine().split(": ", 2)[1].trim();

                    // Determine product type by quantity format
                    if (quantityLine.endsWith("τεμάχια")) {
                        int pieces = Integer.parseInt(quantityLine.replace("τεμάχια", "").trim());
                        products.add(new PieceProduct(title, description, category, subcategory, price, pieces));
                    } else if (quantityLine.endsWith("kg") || quantityLine.endsWith("κιλά")) {
                        double weight = Double.parseDouble(quantityLine.replace("κιλά", "").replace("kg", "").trim());
                        products.add(new WeightProduct(title, description, category, subcategory, price, weight));
                    } else {
                        throw new IllegalArgumentException("Invalid quantity format: " + quantityLine);
                    }

                    // Skip the empty line between products
                    reader.readLine();
                } catch (Exception e) {
                    System.err.println("Error parsing product entry: " + e.getMessage());
                    // Skip to the next product entry
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading products file: " + e.getMessage());
            throw e;
        }

        return products;
    }

    /**
     * Saves a list of products to a file.
     * The output format matches the input format used by loadProducts.
     *
     * @param products the list of products to save
     * @param filePath the path to the file where products will be saved
     * @throws IOException if an I/O error occurs
     */
    public static void saveProducts(List<Product> products, String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : products) {
                writer.write("Title: " + product.getTitle());
                writer.newLine();
                writer.write("Description: " + product.getDescription());
                writer.newLine();
                writer.write("Category: " + product.getCategory());
                writer.newLine();
                writer.write("Subcategory: " + product.getSubcategory());
                writer.newLine();
                writer.write("Price: €" + String.format("%.2f", product.getPrice()));
                writer.newLine();

                if (product instanceof WeightProduct) {
                    writer.write("Quantity: " + ((WeightProduct) product).getAvailableWeight() + " κιλά");
                } else if (product instanceof PieceProduct) {
                    writer.write("Quantity: " + ((PieceProduct) product).getAvailablePieces() + " τεμάχια");
                }

                writer.newLine();
                writer.newLine(); // Separate entries with an empty line
            }
        }
    }
}
