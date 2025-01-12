package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A frame for searching products by title, category, and subcategory.
 */
public class ProductSearchFrame extends JFrame {

    /**
     * Constructs the ProductSearchFrame.
     *
     * @param products     the list of products to search
     * @param isAdmin      whether the user is an administrator
     * @param customerCart the cart of the customer (null if admin)
     */
    public ProductSearchFrame(List<Product> products, boolean isAdmin, Cart customerCart) {
        setTitle("Product Search");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = new JLabel("Search Products", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        searchPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JLabel titleLbl = new JLabel("Title:");
        JTextField titleField = new JTextField();
        titleField.setToolTipText("Enter the product title or leave blank for all products.");

        JLabel categoryLbl = new JLabel("Category:");
        JComboBox<String> categoryComboBox = new JComboBox<>();
        categoryComboBox.setToolTipText("Select a category or leave blank.");
        categoryComboBox.addItem(""); // Empty option for "no category"
        Categories.getCategories().keySet().forEach(categoryComboBox::addItem);

        JLabel subcategoryLbl = new JLabel("Subcategory:");
        JComboBox<String> subcategoryComboBox = new JComboBox<>();
        subcategoryComboBox.setToolTipText("Select a subcategory or leave blank.");
        subcategoryComboBox.addItem(""); // Empty option for "no subcategory"

        // Dynamic subcategory loading
        categoryComboBox.addActionListener(e -> updateSubcategories(categoryComboBox, subcategoryComboBox));

        searchPanel.add(titleLbl);
        searchPanel.add(titleField);
        searchPanel.add(categoryLbl);
        searchPanel.add(categoryComboBox);
        searchPanel.add(subcategoryLbl);
        searchPanel.add(subcategoryComboBox);
        add(searchPanel, BorderLayout.NORTH);

        // Product List Panel
        DefaultListModel<Product> productListModel = new DefaultListModel<>();
        JList<Product> productList = new JList<>(productListModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setFont(new Font("Arial", Font.PLAIN, 14));
        productList.setToolTipText("Select a product from the list to view details.");

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Match search panel background

        JButton searchButton = new JButton("Search");
        JButton viewButton = new JButton("View Product");

        // Style buttons
        styleButton(searchButton);
        styleButton(viewButton);

        buttonPanel.add(searchButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Search Button Action
        searchButton.addActionListener(e -> handleSearch(titleField, categoryComboBox, subcategoryComboBox, products, productListModel));

        // View Button Action
        viewButton.addActionListener(e -> handleViewProduct(productList, isAdmin, customerCart));

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    /**
     * Updates the subcategory combo box based on the selected category.
     *
     * @param categoryComboBox    the category combo box
     * @param subcategoryComboBox the subcategory combo box
     */
    private void updateSubcategories(JComboBox<String> categoryComboBox, JComboBox<String> subcategoryComboBox) {
        subcategoryComboBox.removeAllItems();
        subcategoryComboBox.addItem(""); // Empty option
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            String[] subcategories = Categories.getCategories().get(selectedCategory);
            if (subcategories != null) {
                for (String subcategory : subcategories) {
                    subcategoryComboBox.addItem(subcategory);
                }
            }
        }
    }

    /**
     * Handles the search operation and populates the product list with results.
     *
     * @param titleField          the title search field
     * @param categoryComboBox    the category combo box
     * @param subcategoryComboBox the subcategory combo box
     * @param products            the list of products
     * @param productListModel    the product list model
     */
    private void handleSearch(JTextField titleField, JComboBox<String> categoryComboBox, JComboBox<String> subcategoryComboBox,
                              List<Product> products, DefaultListModel<Product> productListModel) {
        String title = titleField.getText().trim();
        String category = (String) categoryComboBox.getSelectedItem();
        String subcategory = (String) subcategoryComboBox.getSelectedItem();

        // Normalize inputs (null for blank values)
        category = (category != null && !category.isEmpty()) ? category : null;
        subcategory = (subcategory != null && !subcategory.isEmpty()) ? subcategory : null;

        // Search products
        List<Product> searchResults = ProductSearch.searchProducts(products, title, category, subcategory);
        productListModel.clear();

        // Display results
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products found matching the criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            searchResults.forEach(productListModel::addElement);
        }
    }

    /**
     * Handles the view product operation, opening the ProductViewFrame for the selected product.
     *
     * @param productList  the product list
     * @param isAdmin      whether the user is an administrator
     * @param customerCart the customer's cart
     */
    private void handleViewProduct(JList<Product> productList, boolean isAdmin, Cart customerCart) {
        Product selectedProduct = productList.getSelectedValue();
        if (selectedProduct != null) {
            new ProductViewFrame(selectedProduct, isAdmin, customerCart);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to view!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Styles a JButton with custom colors and hover effects.
     *
     * @param button the button to style
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 102, 0)); // Orange color
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(204, 51, 0))); // Darker border

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 153, 51)); // Lighter orange
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 102, 0)); // Original orange
            }
        });
    }
}
