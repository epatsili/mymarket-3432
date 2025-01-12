package gui;

import api.*;

import javax.swing.*;
import java.awt.*;

/**
 * The main entry frame for the Supermarket e-Shop application.
 */
public class MainFrame extends JFrame {
    private final AppInitializer initializer;

    /**
     * Constructs the MainFrame and initializes the application.
     */
    public MainFrame() {
        // Initialize the application
        initializer = new AppInitializer();

        // Set up the frame
        setTitle("Supermarket e-Shop");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Add padding around components

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Supermarket e-Shop", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Add padding
        buttonsPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register as Customer");

        // Set button styles
        styleButton(loginButton);
        styleButton(registerButton);

        // Action for login button
        loginButton.addActionListener(e -> openLoginFrame());

        // Action for register button
        registerButton.addActionListener(e -> openCustomerRegistrationFrame());

        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);
        add(buttonsPanel, BorderLayout.CENTER);

        // Footer label
        JLabel footerLabel = new JLabel("© 2025 Supermarket e-Shop", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(102, 51, 0)); // Brown color
        add(footerLabel, BorderLayout.SOUTH);

        // Center the frame and make it visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Opens the LoginFrame.
     */
    private void openLoginFrame() {
        SwingUtilities.invokeLater(() -> new LoginFrame(initializer.getUserManager(), initializer.getProducts()));
        this.dispose(); // Close MainFrame
    }

    /**
     * Opens the CustomerRegistrationFrame.
     */
    private void openCustomerRegistrationFrame() {
        SwingUtilities.invokeLater(() -> new CustomerRegistrationFrame(initializer.getUserManager()));
        this.dispose(); // Close MainFrame
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

    /**
     * Main method to launch the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
