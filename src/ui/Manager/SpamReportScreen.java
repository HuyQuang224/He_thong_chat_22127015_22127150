import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class SpamReportScreen extends JPanel {
    private JTable reportTable;
    private JTextField searchField, timeField, userField; // Added JTextField for username
    private JButton sortByTimeButton, sortByUserButton, filterByTimeButton, filterByUserButton, lockAccountButton;

    public SpamReportScreen() {
        setLayout(new BorderLayout());

        // Create columns for the spam report table
        String[] columnNames = {"Time", "Username", "Full Name", "Description", "Status"};
        Object[][] data = {
            {"15/11/2024 12:00", "user1", "Nguyen A", "Spam message 1", "Unprocessed"},
            {"15/11/2024 10:00", "user2", "Tran B", "Spam message 2", "Processed"},
            {"14/11/2024 16:30", "user3", "Le C", "Spam message 3", "Unprocessed"},
            {"14/11/2024 15:45", "user4", "Hoang D", "Spam message 4", "Processed"},
        };

        // Create spam report table
        reportTable = new JTable(new DefaultTableModel(data, columnNames));
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(reportTable.getModel());
        reportTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create panel for search and filter
        JPanel filterPanel = new JPanel();

        // Search
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        
        filterPanel.add(searchPanel);

        // Filter buttons
        JPanel filterButtonPanel = new JPanel();
        filterByTimeButton = new JButton("Filter by time");
        filterButtonPanel.add(filterByTimeButton);
        filterByUserButton = new JButton("Filter by username");
        filterButtonPanel.add(filterByUserButton);
        
        filterPanel.add(filterButtonPanel);

        add(filterPanel, BorderLayout.NORTH);

        // Create panel for sorting and locking accounts
        JPanel actionPanel = new JPanel();

        // Sorting buttons
        JPanel sortPanel = new JPanel();
        sortByTimeButton = new JButton("Sort by time");
        sortPanel.add(sortByTimeButton);
        sortByUserButton = new JButton("Sort by username");
        sortPanel.add(sortByUserButton);
        
        actionPanel.add(sortPanel);

        // Lock account button
        JPanel lockPanel = new JPanel();
        lockAccountButton = new JButton("Lock account");
        lockPanel.add(lockAccountButton);
        actionPanel.add(lockPanel);

        add(actionPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Spam Report List");
        SpamReportScreen spamReportPanel = new SpamReportScreen();
        frame.add(spamReportPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
