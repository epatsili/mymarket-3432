package gui;

import api.*;

import javax.swing.*;
import java.awt.*;

/**
 * A frame for viewing product details and performing actions based on user roles.
 */
public class ProductViewFrame extends JFrame {

    /**
     * Constructs the ProductViewFrame.
     *
     * @param product      the product to view
     * @param isAdmin      whether the user is an administrator
     * @param customerCart the cart of the customer (null if admin)
     */
    public ProductViewFrame(Product product, boolean isAdmin, Cart customerCart) {
        setTitle("Product Details");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = createTitleLabel("Product Details");
        add(titleLabel, BorderLayout.NORTH);

        // Details Panel
        JPanel detailsPanel = createDetailsPanel(product);
        add(detailsPanel, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = createActionPanel(product, isAdmin, customerCart);
        add(actionPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    /**
     * Creates a title label.
     *
     * @param text the text for the title label
     * @return the JLabel instance
     */
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(new Color(255, 102, 102)); // Pink color
        return label;
    }

    /**
     * Creates the details panel for the product.
     *
     * @param product the product to display details for
     * @return the JPanel instance
     */
    private JPanel createDetailsPanel(Product product) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        panel.setBackground(new Color(255, 204, 153)); // Light orange background

        panel.add(new JLabel("Title:"));
        panel.add(new JLabel(product.getTitle()));

        panel.add(new JLabel("Description:"));
        panel.add(new JLabel(product.getDescription()));

        panel.add(new JLabel("Category:"));
        panel.add(new JLabel(product.getCategory()));

        panel.add(new JLabel("Subcategory:"));
        panel.add(new JLabel(product.getSubcategory()));

        panel.add(new JLabel("Price (€):"));
        panel.add(new JLabel(String.format("%.2f", product.getPrice())));

        panel.add(new JLabel("Available Quantity:"));
        if (product instanceof PieceProduct) {
            panel.add(new JLabel(((PieceProduct) product).getAvailablePieces() + " pieces"));
        } else if (product instanceof WeightProduct) {
            panel.add(new JLabel(String.format("%.2f", ((WeightProduct) product).getAvailableWeight()) + " kg"));
        }

        return panel;
    }

    /**
     * Creates the action panel for the product based on user role.
     *
     * @param product      the product to view
     * @param isAdmin      whether the user is an administrator
     * @param customerCart the cart of the customer (null if admin)
     * @return the JPanel instance
     */
    private JPanel createActionPanel(Product product, boolean isAdmin, Cart customerCart) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(255, 204, 153)); // Match details panel background

        if (isAdmin) {
            JButton editButton = new JButton("Edit Product");
            styleButton(editButton);
            editButton.addActionListener(e -> {
                new ProductEditFrame(product);
                dispose();
            });
            panel.add(editButton);
        } else {
            JLabel purchaseLbl = new JLabel("Quantity to Purchase:");
            JTextField purchaseField = new JTextField(10);
            JButton addToCartButton = new JButton("Add to Cart");

            styleButton(addToCartButton);

            addToCartButton.addActionListener(e -> handleAddToCart(product, purchaseField, customerCart));

            panel.add(purchaseLbl);
            panel.add(purchaseField);
            panel.add(addToCartButton);
        }

        return panel;
    }

    /**
     * Handles adding a product to the customer's cart.
     *
     * @param product       the product to add
     * @param purchaseField the field for quantity input
     * @param customerCart  the customer's cart
     */
    private void handleAddToCart(Product product, JTextField purchaseField, Cart customerCart) {
        try {
            double quantity = Double.parseDouble(purchaseField.getText().trim());
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");

            if (product instanceof PieceProduct) {
                PieceProduct pieceProduct = (PieceProduct) product;
                if (pieceProduct.validatePurchase((int) quantity)) {
                    pieceProduct.reduceStock((int) quantity);
                    customerCart.addProduct(pieceProduct, (int) quantity);
                    JOptionPane.showMessageDialog(this, "Product added to cart!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock available!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (product instanceof WeightProduct) {
                WeightProduct weightProduct = (WeightProduct) product;
                if (weightProduct.validatePurchase(quantity)) {
                    weightProduct.reduceStock(quantity);
                    customerCart.addProduct(weightProduct, quantity);
                    JOptionPane.showMessageDialog(this, "Product added to cart!");
                } else {
                    JOptionPane.showMessageDialog(this, "Not enough stock available!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format! Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
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
