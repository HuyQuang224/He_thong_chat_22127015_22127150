import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class NewUserRegistrationScreen extends JPanel {
    private JTable userTable;
    private JTextField searchField; // Search field by name
    private JComboBox<String> sortByComboBox; // ComboBox for selecting sorting by name or registration date
    private DefaultTableModel tableModel;
    private JComboBox<String> yearComboBox; // ComboBox for selecting year
    private JButton showChartButton; // Button to display chart
    private JButton filterByNameButton; // Button to filter by name

    public NewUserRegistrationScreen() {
        setLayout(new BorderLayout());

        // Create columns for the new user registration table
        String[] columnNames = {"Username", "Full Name", "Email", "Registration Date"};
        Object[][] data = {
            {"user1", "Nguyen A", "user1@email.com", "01/12/2024"},
            {"user2", "Tran B", "user2@email.com", "02/12/2024"},
            {"user3", "Le C", "user3@email.com", "15/11/2024"},
            {"user4", "Hoang D", "user4@email.com", "20/10/2024"},
        };

        // Create DefaultTableModel and user table
        tableModel = new DefaultTableModel(data, columnNames);
        userTable = new JTable(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create search field for name filtering
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by name:"));
        searchPanel.add(searchField);
        
        // Add filter by name button
        filterByNameButton = new JButton("Filter by name");
        searchPanel.add(filterByNameButton);

        add(searchPanel, BorderLayout.NORTH);

        // ComboBox for sorting by name or registration date
        String[] sortOptions = {"Sort by name", "Sort by registration date"};
        sortByComboBox = new JComboBox<>(sortOptions);
        searchPanel.add(new JLabel("Sort by:"));
        searchPanel.add(sortByComboBox);

        // ComboBox to select year
        yearComboBox = new JComboBox<>(new String[]{"2024", "2023", "2022"});
        searchPanel.add(new JLabel("Select year:"));
        searchPanel.add(yearComboBox);

        // Add JPanel containing button to BorderLayout.SOUTH
        JPanel buttonPanel = new JPanel();
        showChartButton = new JButton("Show Chart");
        buttonPanel.add(showChartButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Filter table by name
    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) userTable.getRowSorter();
        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(query);
        sorter.setRowFilter(rowFilter);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("New User Registration List");
        NewUserRegistrationScreen panel = new NewUserRegistrationScreen();
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
