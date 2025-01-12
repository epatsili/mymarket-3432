package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main dashboard for administrators to manage products and view statistics.
 */
public class AdminMainFrame extends JFrame {

    /**
     * Constructs the AdminMainFrame.
     *
     * @param admin      the logged-in administrator
     * @param userManager the user manager for managing users
     * @param products   the list of products
     */
    public AdminMainFrame(Administrator admin, UserManager userManager, List<Product> products) {
        setTitle("Administrator Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title label
        JLabel titleLabel = new JLabel("Welcome, " + admin.getUsername() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Add padding
        buttonsPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JButton manageProductsButton = new JButton("Manage Products");
        JButton viewStatisticsButton = new JButton("View Statistics");
        JButton logoutButton = new JButton("Logout");

        // Style the buttons
        styleButton(manageProductsButton);
        styleButton(viewStatisticsButton);
        styleButton(logoutButton);

        // Action for managing products
        manageProductsButton.addActionListener(e -> openProductRegistrationFrame(products));

        // Action for viewing statistics
        viewStatisticsButton.addActionListener(e -> showStatistics(products));

        // Action for logout
        logoutButton.addActionListener(e -> logout(userManager, products));

        // Add buttons to panel
        buttonsPanel.add(manageProductsButton);
        buttonsPanel.add(viewStatisticsButton);
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
     * Opens the ProductRegistrationFrame for managing products.
     *
     * @param products the list of products
     */
    private void openProductRegistrationFrame(List<Product> products) {
        SwingUtilities.invokeLater(() -> new ProductRegistrationFrame(products));
    }

    /**
     * Displays statistics about the products.
     *
     * @param products the list of products
     */
    private void showStatistics(List<Product> products) {
        StringBuilder statistics = new StringBuilder("Statistics:\n");

        // Count unavailable products
        long unavailableProducts = products.stream()
                .filter(product -> {
                    if (product instanceof PieceProduct) {
                        return ((PieceProduct) product).getAvailablePieces() == 0;
                    } else if (product instanceof WeightProduct) {
                        return ((WeightProduct) product).getAvailableWeight() == 0.0;
                    }
                    return false;
                })
                .count();
        statistics.append("Unavailable Products: ").append(unavailableProducts).append("\n");

        // Find most ordered products (placeholder logic)
        // Extend this with order data integration in the future
        List<String> mostOrderedProducts = products.stream()
                .limit(3) // Placeholder for top 3 most ordered products
                .map(Product::getTitle)
                .collect(Collectors.toList());
        statistics.append("Most Ordered Products: ").append(String.join(", ", mostOrderedProducts));

        JOptionPane.showMessageDialog(this, statistics.toString(), "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Logs out the user and redirects to the LoginFrame.
     *
     * @param userManager the UserManager instance
     * @param products    the list of products
     */
    private void logout(UserManager userManager, List<Product> products) {
        dispose(); // Close the admin dashboard
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
