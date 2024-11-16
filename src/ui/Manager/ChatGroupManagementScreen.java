import java.awt.*;
import javax.swing.*;

public class ChatGroupManagementScreen extends JFrame {
    public ChatGroupManagementScreen() {
        setTitle("Chat Group Management");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Group search panel
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Search group:"));
        JTextField searchField = new JTextField(20);
        filterPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        filterPanel.add(searchButton);

        // Filter by group name button (just add the button without any action)
        JButton filterByNameButton = new JButton("Filter by name");
        filterPanel.add(filterByNameButton);

        // Table to display the list of groups
        String[] columnNames = {"Group Name", "Creation Date"};
        Object[][] data = {
            {"Group A", "15/01/2024"},
            {"Group B", "02/12/2024"},
            {"Group C", "02/02/2024"},
            {"Group D", "05/05/2024"}
        };
        JTable groupTable = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(groupTable);

        // Function buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewMembersButton = new JButton("View members");
        JButton viewAdminsButton = new JButton("View admins");
        buttonPanel.add(viewMembersButton);
        buttonPanel.add(viewAdminsButton);

        // Add components to the mainPanel
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add mainPanel to JFrame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatGroupManagementScreen screen = new ChatGroupManagementScreen();
            screen.setVisible(true);
        });
    }
}
