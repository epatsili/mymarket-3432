package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A frame for registering new products.
 */
public class ProductRegistrationFrame extends JFrame {
    private final JComboBox<String> categoryComboBox;
    private final JComboBox<String> subcategoryComboBox;
    private final JTextField titleField, descriptionField, priceField, quantityField;
    private final JRadioButton pieceRadioButton, weightRadioButton;

    /**
     * Constructs the ProductRegistrationFrame.
     *
     * @param products the list of existing products
     */
    public ProductRegistrationFrame(List<Product> products) {
        setTitle("Product Registration");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = new JLabel("Register a New Product", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        formPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        // Form Fields
        titleField = createTextField("Enter the product title.");
        descriptionField = createTextField("Enter a brief description of the product.");
        priceField = createTextField("Enter the product price (e.g., 12.50).");
        quantityField = createTextField("Enter the quantity (pieces or weight in kg).");

        categoryComboBox = createCategoryComboBox();
        subcategoryComboBox = createSubcategoryComboBox();

        pieceRadioButton = new JRadioButton("Pieces");
        weightRadioButton = new JRadioButton("Weight (kg)");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(pieceRadioButton);
        typeGroup.add(weightRadioButton);

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);
        formPanel.add(new JLabel("Subcategory:"));
        formPanel.add(subcategoryComboBox);
        formPanel.add(new JLabel("Price (€):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(pieceRadioButton);
        formPanel.add(weightRadioButton);

        add(formPanel, BorderLayout.CENTER);

        // Dynamic Subcategory Loading
        categoryComboBox.addActionListener(e -> updateSubcategories());

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Match form panel background
        JButton registerButton = new JButton("Register Product");
        styleButton(registerButton);

        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Register Button Action
        registerButton.addActionListener(e -> handleProductRegistration(products));

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    /**
     * Creates a text field with a tooltip.
     *
     * @param toolTip the tooltip text
     * @return the created text field
     */
    private JTextField createTextField(String toolTip) {
        JTextField textField = new JTextField();
        textField.setToolTipText(toolTip);
        return textField;
    }

    /**
     * Creates and initializes the category combo box.
     *
     * @return the created combo box
     */
    private JComboBox<String> createCategoryComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setToolTipText("Select the product category.");
        for (String category : Categories.getCategories().keySet()) {
            comboBox.addItem(category);
        }
        return comboBox;
    }

    /**
     * Creates an empty subcategory combo box.
     *
     * @return the created combo box
     */
    private JComboBox<String> createSubcategoryComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setToolTipText("Select the product subcategory.");
        return comboBox;
    }

    /**
     * Updates the subcategories based on the selected category.
     */
    private void updateSubcategories() {
        subcategoryComboBox.removeAllItems();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory != null) {
            String[] subcategories = Categories.getCategories().get(selectedCategory);
            for (String subcategory : subcategories) {
                subcategoryComboBox.addItem(subcategory);
            }
        }
    }

    /**
     * Handles the product registration process.
     *
     * @param products the list of existing products
     */
    private void handleProductRegistration(List<Product> products) {
        try {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String category = (String) categoryComboBox.getSelectedItem();
            String subcategory = (String) subcategoryComboBox.getSelectedItem();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (title.isEmpty() || description.isEmpty() || category == null || subcategory == null || priceText.isEmpty() || quantityText.isEmpty()) {
                throw new IllegalArgumentException("All fields are mandatory!");
            }

            double price = Double.parseDouble(priceText);
            if (price <= 0) throw new IllegalArgumentException("Price must be greater than zero.");

            if (pieceRadioButton.isSelected()) {
                int pieces = Integer.parseInt(quantityText);
                if (pieces < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
                products.add(new PieceProduct(title, description, category, subcategory, price, pieces));
                JOptionPane.showMessageDialog(this, "Product registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (weightRadioButton.isSelected()) {
                double weight = Double.parseDouble(quantityText);
                if (weight < 0) throw new IllegalArgumentException("Weight cannot be negative.");
                products.add(new WeightProduct(title, description, category, subcategory, price, weight));
                JOptionPane.showMessageDialog(this, "Product registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new IllegalArgumentException("Please select a product type.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for price or quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
