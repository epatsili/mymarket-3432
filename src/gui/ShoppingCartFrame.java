package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * A frame to display and manage the customer's shopping cart.
 */
public class ShoppingCartFrame extends JFrame {
    private Cart cart;
    private Customer customer;
    private DefaultListModel<String> cartModel;
    private JLabel totalCostLabel;

    /**
     * Constructs the ShoppingCartFrame.
     *
     * @param cart     the customer's shopping cart
     * @param customer the customer using the shopping cart
     */
    public ShoppingCartFrame(Cart cart, Customer customer) {
        this.cart = cart;
        this.customer = customer;

        setTitle("Shopping Cart");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = createTitleLabel("Your Shopping Cart");
        add(titleLabel, BorderLayout.NORTH);

        // Cart Display Panel
        cartModel = new DefaultListModel<>();
        JList<String> cartList = createCartList();
        JScrollPane scrollPane = new JScrollPane(cartList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cart Items"));
        add(scrollPane, BorderLayout.CENTER);

        // Action Buttons Panel
        JPanel buttonPanel = createButtonPanel(cartList);
        add(buttonPanel, BorderLayout.SOUTH);

        // Total Cost Display
        totalCostLabel = createTotalCostLabel();
        add(totalCostLabel, BorderLayout.SOUTH);

        updateCartDisplay();
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
     * Creates the cart list for displaying products in the cart.
     *
     * @return the JList instance
     */
    private JList<String> createCartList() {
        JList<String> cartList = new JList<>(cartModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartList.setFont(new Font("Arial", Font.PLAIN, 14));
        cartList.setToolTipText("Select a product to update or remove.");
        return cartList;
    }

    /**
     * Creates the button panel with action buttons.
     *
     * @param cartList the cart list for selection
     * @return the JPanel instance
     */
    private JPanel createButtonPanel(JList<String> cartList) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(255, 204, 153)); // Light orange background

        JButton updateButton = new JButton("Update Quantity");
        JButton deleteButton = new JButton("Delete Product");
        JButton checkoutButton = new JButton("Complete Order");

        // Style buttons
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(checkoutButton);

        updateButton.addActionListener(e -> handleUpdateQuantity(cartList));
        deleteButton.addActionListener(e -> handleDeleteProduct(cartList));
        checkoutButton.addActionListener(e -> handleCheckout());

        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(checkoutButton);
        return panel;
    }

    /**
     * Creates the total cost label.
     *
     * @return the JLabel instance
     */
    private JLabel createTotalCostLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        return label;
    }

    /**
     * Updates the cart display and total cost label.
     */
    private void updateCartDisplay() {
        cartModel.clear();

        if (cart.getProducts().isEmpty()) {
            cartModel.addElement("The cart is empty.");
            totalCostLabel.setText("Total Cost: €0.00");
            return;
        }

        for (Map.Entry<Product, Double> entry : cart.getProducts().entrySet()) {
            Product product = entry.getKey();
            Double quantity = entry.getValue();
            double totalCost = quantity * product.getPrice();
            cartModel.addElement(product.getTitle() + " - " + quantity + " - €" + String.format("%.2f", totalCost));
        }

        totalCostLabel.setText("Total Cost: €" + String.format("%.2f", cart.getTotalCost()));
    }

    /**
     * Handles updating the quantity of a product in the cart.
     *
     * @param cartList the list of products in the cart
     */
    private void handleUpdateQuantity(JList<String> cartList) {
        String selectedValue = cartList.getSelectedValue();
        if (selectedValue == null) {
            showErrorDialog("Please select a product to update!");
            return;
        }

        String productName = selectedValue.split(" - ")[0];
        Product selectedProduct = findProductByName(productName);

        if (selectedProduct != null) {
            String quantityStr = JOptionPane.showInputDialog(this, "Enter new quantity:", "Update Quantity", JOptionPane.QUESTION_MESSAGE);
            if (quantityStr == null || quantityStr.trim().isEmpty()) {
                showErrorDialog("Quantity cannot be empty!");
                return;
            }

            try {
                double newQuantity = Double.parseDouble(quantityStr.trim());
                if (newQuantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");

                validateAndApplyNewQuantity(selectedProduct, newQuantity);
                updateCartDisplay();
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid number format!");
            } catch (IllegalArgumentException ex) {
                showErrorDialog(ex.getMessage());
            }
        }
    }

    /**
     * Validates and applies the new quantity for the selected product.
     *
     * @param product     the product to update
     * @param newQuantity the new quantity
     */
    private void validateAndApplyNewQuantity(Product product, double newQuantity) {
        double availableStock = (product instanceof PieceProduct)
                ? ((PieceProduct) product).getAvailablePieces()
                : ((WeightProduct) product).getAvailableWeight();

        double currentQuantityInCart = cart.getProducts().getOrDefault(product, 0.0);

        if (newQuantity <= availableStock + currentQuantityInCart) {
            cart.updateProductQuantity(product, newQuantity);
        } else {
            throw new IllegalArgumentException("Not enough stock available!");
        }
    }

    /**
     * Handles deleting a product from the cart.
     *
     * @param cartList the list of products in the cart
     */
    private void handleDeleteProduct(JList<String> cartList) {
        String selectedValue = cartList.getSelectedValue();
        if (selectedValue == null) {
            showErrorDialog("Please select a product to delete!");
            return;
        }

        String productName = selectedValue.split(" - ")[0];
        Product selectedProduct = findProductByName(productName);

        if (selectedProduct != null) {
            cart.removeProduct(selectedProduct);
            updateCartDisplay();
        }
    }

    /**
     * Handles completing the order and clearing the cart.
     */
    private void handleCheckout() {
        if (cart.getProducts().isEmpty()) {
            showErrorDialog("Your cart is empty!");
            return;
        }

        Order newOrder = new Order(cart.getProducts());
        customer.getOrderHistory().add(newOrder);
        cart.clearCart();
        updateCartDisplay();
        // Notify the user
        JOptionPane.showMessageDialog(this, "Order completed! Total cost: €" +
                String.format("%.2f", newOrder.getTotalCost()), "Order Completed", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Finds a product in the cart by its name.
     *
     * @param name the name of the product
     * @return the matching product, or null if not found
     */
    private Product findProductByName(String name) {
        for (Product product : cart.getProducts().keySet()) {
            if (product.getTitle().equals(name)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message the error message
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
