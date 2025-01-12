package gui;

import api.*;

import javax.swing.*;
import java.awt.*;

/**
 * A frame for editing a product's details.
 */
public class ProductEditFrame extends JFrame {

    /**
     * Constructs the ProductEditFrame.
     *
     * @param product the product to edit
     */
    public ProductEditFrame(Product product) {
        setTitle("Edit Product");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = new JLabel("Edit Product Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        inputPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        // Input Fields
        JTextField titleField = createTextField(product.getTitle(), "Enter the product title.");
        JTextField descriptionField = createTextField(product.getDescription(), "Enter the product description.");
        JTextField priceField = createTextField(String.valueOf(product.getPrice()), "Enter the product price in euros.");
        JTextField quantityField = createTextField(
                product instanceof PieceProduct ? String.valueOf(((PieceProduct) product).getAvailablePieces())
                        : String.valueOf(((WeightProduct) product).getAvailableWeight()),
                "Enter the product quantity.");

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Price (€):"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        add(inputPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Match input panel background
        JButton saveButton = new JButton("Save Changes");

        // Style the save button
        styleButton(saveButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Save Button Action
        saveButton.addActionListener(e -> handleSaveAction(product, titleField, descriptionField, priceField, quantityField));

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    /**
     * Creates a styled JTextField with an initial value and tooltip.
     *
     * @param initialValue the initial value of the field
     * @param toolTip      the tooltip text
     * @return the created JTextField
     */
    private JTextField createTextField(String initialValue, String toolTip) {
        JTextField textField = new JTextField(initialValue);
        textField.setToolTipText(toolTip);
        return textField;
    }

    /**
     * Handles the save action for the product.
     *
     * @param product       the product to update
     * @param titleField    the title input field
     * @param descriptionField the description input field
     * @param priceField    the price input field
     * @param quantityField the quantity input field
     */
    private void handleSaveAction(Product product, JTextField titleField, JTextField descriptionField, JTextField priceField, JTextField quantityField) {
        try {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();
            String priceText = priceField.getText().trim();
            String quantityText = quantityField.getText().trim();

            if (title.isEmpty() || description.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                throw new IllegalArgumentException("All fields are mandatory.");
            }

            double price = parsePositiveDouble(priceText, "Price must be a positive number.");
            double quantity = parsePositiveDouble(quantityText, "Quantity must be a positive number.");

            product.setTitle(title);
            product.setDescription(description);
            product.setPrice(price);

            if (product instanceof PieceProduct) {
                ((PieceProduct) product).setAvailablePieces((int) quantity);
            } else if (product instanceof WeightProduct) {
                ((WeightProduct) product).setAvailableWeight(quantity);
            }

            JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please check the price and quantity fields.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Parses a string into a positive double.
     *
     * @param text the text to parse
     * @param errorMessage the error message to display if the number is invalid
     * @return the parsed double
     * @throws NumberFormatException if the text is not a valid number
     * @throws IllegalArgumentException if the number is not positive
     */
    private double parsePositiveDouble(String text, String errorMessage) {
        double value = Double.parseDouble(text);
        if (value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
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
