import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class UserActivityGUI extends JFrame {

    private JTable userTable;
    private JTextField searchField, filterActivityField, filterComparisonField;
    private JButton filterButton, plotButton, showChartButton;
    private JPanel panel;
    private JPanel chartPanel;
    private JPanel searchPanel; // Panel for search and filter

    public UserActivityGUI() {
        setTitle("User Activity List");
        setLayout(new BorderLayout());

        // Sample user data for the demonstration
        String[] columnNames = {"Username", "Full Name", "App Opened", "Chat Messages", "Chat Groups", "Creation Date"};
        Object[][] data = {
            {"user1", "Nguyen A", 10, 5, 3, "2022-11-01"},
            {"user2", "Tran B", 8, 4, 2, "2023-01-10"},
            {"user3", "Le C", 12, 6, 4, "2023-05-15"},
            {"user4", "Hoang D", 5, 2, 1, "2024-03-15"}
        };

        // Create the table with the sample data
        userTable = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create a panel for the search and filter controls
        searchPanel = new JPanel();
    

        // Add search field for searching by name
        JPanel searchRow = new JPanel();
        searchRow.add(new JLabel("Search by name:"));
        searchField = new JTextField(15);
        searchRow.add(searchField);
        searchPanel.add(searchRow);

        // Add filter button
        JPanel filterButtonRow = new JPanel();
        filterButton = new JButton("Filter");
        filterButtonRow.add(filterButton);
        searchPanel.add(filterButtonRow);

        // Add the search panel at the top of the frame
        add(searchPanel, BorderLayout.NORTH);

        // Panel for displaying chart (Placeholder for future chart display)
        chartPanel = new JPanel();

        // Create a button panel for showing chart
        JPanel buttonPanel = new JPanel();
        showChartButton = new JButton("Show Chart");
        buttonPanel.add(showChartButton);
        add(buttonPanel, BorderLayout.SOUTH);  // Add the buttonPanel at the bottom of the chartPanel


        // Window settings
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserActivityGUI gui = new UserActivityGUI();
            gui.setVisible(true);
        });
    }
}
