package gui;

import api.*;

import javax.swing.*;
import java.awt.*;

/**
 * A frame for registering new customers.
 */
public class CustomerRegistrationFrame extends JFrame {
    private final UserManager userManager;

    /**
     * Constructs the CustomerRegistrationFrame.
     *
     * @param userManager the UserManager instance for managing users
     */
    public CustomerRegistrationFrame(UserManager userManager) {
        this.userManager = userManager;

        setTitle("Customer Registration");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel titleLabel = new JLabel("Register as a New Customer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Input Fields Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        inputPanel.setBackground(new Color(255, 204, 153)); // Light orange background

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        firstNameField.setToolTipText("Enter your first name.");

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        lastNameField.setToolTipText("Enter your last name.");

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        usernameField.setToolTipText("Choose a unique username.");

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setToolTipText("Choose a strong password.");

        inputPanel.add(firstNameLabel);
        inputPanel.add(firstNameField);
        inputPanel.add(lastNameLabel);
        inputPanel.add(lastNameField);
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Match input panel background
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        // Style buttons
        styleButton(registerButton);
        styleButton(backButton);

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Register Action
        registerButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are mandatory!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isPasswordStrong(password)) {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and contain a mix of letters and numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Customer newCustomer = new Customer(username, password);
                userManager.addUser(newCustomer);
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                SwingUtilities.invokeLater(MainFrame::new); // Navigate back to MainFrame
            } catch (UserAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back Action
        backButton.addActionListener(e -> {
            SwingUtilities.invokeLater(MainFrame::new);
            dispose();
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

    /**
     * Checks if a password is strong.
     *
     * @param password the password to check
     * @return true if the password is strong, false otherwise
     */
    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }
}
