package gui;

import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * A frame to display the order history of a customer.
 */
public class OrderHistoryFrame extends JFrame {
    private final Customer customer;

    /**
     * Constructs the OrderHistoryFrame.
     *
     * @param customer the customer whose order history is displayed
     */
    public OrderHistoryFrame(Customer customer) {
        this.customer = customer;

        setTitle("Order History");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add spacing

        // Title Label
        JLabel titleLabel = new JLabel("Order History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(255, 102, 102)); // Pink color
        add(titleLabel, BorderLayout.NORTH);

        // Orders List Panel
        DefaultListModel<String> orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(orderList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Orders"));

        // Order Details Panel
        JTextArea orderDetailsArea = new JTextArea();
        orderDetailsArea.setEditable(false);
        orderDetailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane detailsScrollPane = new JScrollPane(orderDetailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Order Details"));

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, detailsScrollPane);
        splitPane.setDividerLocation(250);

        // Load Orders into List
        List<Order> orders = customer.getOrderHistory();
        if (orders.isEmpty()) {
            orderListModel.addElement("No orders found.");
        } else {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                orderListModel.addElement("Order " + (i + 1) + " - " + order.getOrderDate());
            }
        }

        // Display Order Details on Selection
        orderList.addListSelectionListener(e -> {
            int selectedIndex = orderList.getSelectedIndex();
            if (selectedIndex != -1 && !orders.isEmpty()) {
                Order selectedOrder = orders.get(selectedIndex);
                StringBuilder details = new StringBuilder();
                details.append("Order Date: ").append(selectedOrder.getOrderDate()).append("\n");
                details.append("Products:\n");
                for (Map.Entry<Product, Double> entry : selectedOrder.getOrderedProducts().entrySet()) {
                    Product product = entry.getKey();
                    Double quantity = entry.getValue();
                    details.append("- ").append(product.getTitle())
                            .append(": ").append(quantity)
                            .append(product instanceof PieceProduct ? " pieces" : " kg")
                            .append("\n");
                }
                details.append("\nTotal Cost: €").append(String.format("%.2f", selectedOrder.getTotalCost()));
                orderDetailsArea.setText(details.toString());
            } else {
                orderDetailsArea.setText("");
            }
        });

        // Close Button
        JButton closeButton = new JButton("Close");
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 204, 153)); // Light orange background
        buttonPanel.add(closeButton);

        // Add Components to Frame
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the frame
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