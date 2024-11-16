import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class UserManagementScreen extends JPanel {
    private JTable userTable;
    private JTextField searchField;
    private JButton addButton, updateButton, deleteButton, lockButton, unlockButton, changePasswordButton;
    private JTable historyTable;

    public UserManagementScreen() {
        setLayout(new BorderLayout());

        // Create user table
        String[] columnNames = {"Username", "Full Name", "Address", "Date of Birth", "Gender", "Email", "Status"};
        Object[][] data = {
            {"user1", "Nguyen A", "123 ABC St.", "01/01/1990", "Male", "user1@email.com", "Active"},
            {"user2", "Tran B", "456 XYZ St.", "02/02/1991", "Female", "user2@email.com", "Locked"},
        };

        // Set up the user table
        userTable = new JTable(new DefaultTableModel(data, columnNames));
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(userTable.getModel());
        userTable.setRowSorter(sorter);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        add(userScrollPane, BorderLayout.CENTER);

        // Create search bar
        searchField = new JTextField(15);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Create action buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        lockButton = new JButton("Lock");
        unlockButton = new JButton("Unlock");
        changePasswordButton = new JButton("Change Password");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(unlockButton);
        buttonPanel.add(changePasswordButton);

        // Login history - Create login history table
        String[] historyColumnNames = {"Timestamp", "Username", "Full Name"};
        Object[][] historyData = {
            {"15/11/2024 10:00", "user1", "Nguyen A"},
            {"15/11/2024 09:45", "user2", "Tran B"},
            {"15/11/2024 08:30", "user3", "Le C"},
            {"14/11/2024 17:00", "user4", "Hoang D"},
        };

        historyTable = new JTable(new DefaultTableModel(historyData, historyColumnNames));
        JScrollPane historyScrollPane = new JScrollPane(historyTable);

        // Create panel for login history table
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(new JLabel("Login History:"), BorderLayout.NORTH);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        // Create a summary panel for buttons and login history
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);  // Buttons on top
        southPanel.add(historyPanel, BorderLayout.CENTER);  // Login history table at the bottom

        // Add the summary panel to the bottom section
        add(southPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("User Management");
        UserManagementScreen userPanel = new UserManagementScreen();
        frame.add(userPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
