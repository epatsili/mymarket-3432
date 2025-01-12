package api;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes the application with default data, including users, products, and categories.
 */
public class AppInitializer implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private static final String PRODUCTS_FILE = "src/products.txt";
    private final UserManager userManager;
    private List<Product> products;

    /**
     * Constructs the AppInitializer and performs initialization tasks.
     */
    public AppInitializer() {
        this.userManager = new UserManager();
        this.products = new ArrayList<>();
        initializeDefaultUsers();
        initializeProducts();
        loadCategories();
    }

    /**
     * Initializes the default users (administrators and customers).
     */
    private void initializeDefaultUsers() {
        try {
            // Add administrators
            if (!userManager.isUsernameTaken("admin1")) {
                userManager.addUser(new Administrator("admin1", "password1"));
            }
            if (!userManager.isUsernameTaken("admin2")) {
                userManager.addUser(new Administrator("admin2", "password2"));
            }

            // Add customers
            if (!userManager.isUsernameTaken("user1")) {
                userManager.addUser(new Customer("user1", "password1"));
            }
            if (!userManager.isUsernameTaken("user2")) {
                userManager.addUser(new Customer("user2", "password2"));
            }

            System.out.println("Default users initialized successfully.");
        } catch (UserAlreadyExistsException e) {
            System.err.println("Error initializing default users: " + e.getMessage());
        }
    }

    /**
     * Loads products from the specified file. If the file doesn't exist, it creates the file with default products.
     */
    private void initializeProducts() {
        File productFile = new File(AppInitializer.PRODUCTS_FILE);
        if (!productFile.exists()) {
            // If the file doesn't exist, save default products
            System.out.println("Product file not found. Creating default product file...");
            saveDefaultProducts();
        }

        // Load products from the file
        try {
            products = ProductLoader.loadProducts(AppInitializer.PRODUCTS_FILE);
            System.out.println("Products loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading products: " + e.getMessage());
        }
    }

    /**
     * Saves default products to the specified file.
     */
    private void saveDefaultProducts() {
        List<Product> defaultProducts = getDefaultProducts();
        try {
            ProductLoader.saveProducts(defaultProducts, AppInitializer.PRODUCTS_FILE);
            System.out.println("Default products saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving default products: " + e.getMessage());
        }
    }

    /**
     * Provides a list of default products.
     *
     * @return a list of default products
     */
    private List<Product> getDefaultProducts() {
        List<Product> defaultProducts = new ArrayList<>();
        defaultProducts.add(new WeightProduct("Πορτοκάλια 1kg", "Φρέσκα πορτοκάλια, ιδανικά για χυμό ή κατανάλωση.",
                "Φρέσκα τρόφιμα", "Φρούτα", 1.20, 200));
        defaultProducts.add(new WeightProduct("Καρότα 1kg", "Τραγανά καρότα, κατάλληλα για σαλάτες και μαγείρεμα.",
                "Φρέσκα τρόφιμα", "Λαχανικά", 1.00, 150));
        defaultProducts.add(new PieceProduct("Φιλέτο Σολομού 300g", "Φρέσκος σολομός φιλέτο έτοιμος για μαγείρεμα.",
                "Φρέσκα τρόφιμα", "Ψάρια", 12.00, 50));
        defaultProducts.add(new PieceProduct("Κιμάς Μοσχαρίσιος 500g", "Φρέσκος κιμάς μοσχαρίσιος από τοπικό κρεοπωλείο.",
                "Φρέσκα τρόφιμα", "Κρέατα", 6.50, 100));
        // Add more products as needed...
        return defaultProducts;
    }

    /**
     * Loads categories from the specified file.
     */
    private void loadCategories() {
        try {
            Categories.loadCategories("src/categories_subcategories.txt");
            System.out.println("Categories loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }

    /**
     * Gets the UserManager instance.
     *
     * @return the UserManager instance
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * Gets the list of initialized products.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products); // Return a copy to maintain encapsulation
    }
}
