package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The main dashboard for customers to search products, view their cart, and order history.
 */
public class CustomerMainFrame extends JFrame {

    /**
     * Constructs the CustomerMainFrame.
     *
     * @param customer   the logged-in customer
     * @param userManager the user manager for managing users
     * @param products   the list of products
     */
    public CustomerMainFrame(Customer customer, UserManager userManager, List<Product> products) {
        setTitle("Customer Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title label
        JLabel titleLabel = new JLabel("Welcome, " + customer.getUsername() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding
        buttonsPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JButton viewProductsButton = new JButton("Search Products");
        JButton viewCartButton = new JButton("View Cart");
        JButton viewOrdersButton = new JButton("Order History");
        JButton logoutButton = new JButton("Logout");

        // Style the buttons
        styleButton(viewProductsButton);
        styleButton(viewCartButton);
        styleButton(viewOrdersButton);
        styleButton(logoutButton);

        // Button Actions
        viewProductsButton.addActionListener(e -> openProductSearchFrame(products, customer));
        viewCartButton.addActionListener(e -> openShoppingCartFrame(customer));
        viewOrdersButton.addActionListener(e -> openOrderHistoryFrame(customer));
        logoutButton.addActionListener(e -> logout(userManager, products));

        // Add buttons to panel
        buttonsPanel.add(viewProductsButton);
        buttonsPanel.add(viewCartButton);
        buttonsPanel.add(viewOrdersButton);
        buttonsPanel.add(logoutButton);

        add(buttonsPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("© 2025 Supermarket e-Shop", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(102, 51, 0)); // Brown color
        add(footerLabel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    /**
     * Opens the ProductSearchFrame.
     *
     * @param products the list of products
     * @param customer the logged-in customer
     */
    private void openProductSearchFrame(List<Product> products, Customer customer) {
        SwingUtilities.invokeLater(() -> new ProductSearchFrame(products, false, customer.getCart()));
    }

    /**
     * Opens the ShoppingCartFrame.
     *
     * @param customer the logged-in customer
     */
    private void openShoppingCartFrame(Customer customer) {
        SwingUtilities.invokeLater(() -> new ShoppingCartFrame(customer.getCart(), customer));
    }

    /**
     * Opens the OrderHistoryFrame.
     *
     * @param customer the logged-in customer
     */
    private void openOrderHistoryFrame(Customer customer) {
        SwingUtilities.invokeLater(() -> new OrderHistoryFrame(customer));
    }

    /**
     * Logs out the user and redirects to the LoginFrame.
     *
     * @param userManager the UserManager instance
     * @param products    the list of products
     */
    private void logout(UserManager userManager, List<Product> products) {
        dispose(); // Close the customer dashboard
        SwingUtilities.invokeLater(() -> new LoginFrame(userManager, products));
    }

    /**
     * Styles a JButton with custom colors and hover effects.
     *
     * @param button the button to style
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
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
