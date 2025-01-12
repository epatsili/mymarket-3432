import api.AppInitializer;
import api.Product;
import api.UserManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AppInitializer initializer = new AppInitializer();
        UserManager userManager = initializer.getUserManager();
        List<Product> products = initializer.getProducts();

        // Example usage
        System.out.println("Initialized Users:");
        System.out.println(userManager);

        System.out.println("Loaded Products:");
        products.forEach(System.out::println);
    }
}
