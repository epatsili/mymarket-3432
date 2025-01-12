package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A frame for user login.
 */
public class LoginFrame extends JFrame {
    private final UserManager userManager;
    private final List<Product> products;

    /**
     * Constructs the LoginFrame.
     *
     * @param userManager the UserManager instance for managing users
     * @param products    the list of products
     */
    public LoginFrame(UserManager userManager, List<Product> products) {
        this.userManager = userManager;
        this.products = products;

        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title label
        JLabel titleLabel = new JLabel("Welcome to the Supermarket App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        inputPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        // Set label fonts
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Match input panel background
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Style the buttons
        styleButton(loginButton);
        styleButton(backButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Login action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User authenticatedUser = userManager.authenticate(username, password);
            if (authenticatedUser != null) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose(); // Close login frame

                if (authenticatedUser instanceof Administrator) {
                    SwingUtilities.invokeLater(() -> new AdminMainFrame((Administrator) authenticatedUser, userManager, products));
                } else if (authenticatedUser instanceof Customer) {
                    SwingUtilities.invokeLater(() -> new CustomerMainFrame((Customer) authenticatedUser, userManager, products));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back action
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(MainFrame::new);
            dispose(); // Close login frame
        });

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
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
