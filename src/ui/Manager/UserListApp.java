import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class UserListApp extends JPanel {
    private JTable userTable;
    private JTextField searchField;
    private JButton addButton, updateButton, deleteButton, filterButton;
    private JTextField filterFriendsField, filterComparisonField;
    private ArrayList<User> users;

    public UserListApp() {
        setLayout(new BorderLayout());

        // Create sample user data (with creation date)
        users = new ArrayList<>();
        users.add(new User("user1", "Nguyen A", 5, "2022-11-01"));
        users.add(new User("user2", "Tran B", 3, "2023-01-10"));
        users.add(new User("user3", "Le C", 10, "2022-07-23"));
        users.add(new User("user4", "Hoang D", 2, "2024-03-15"));

        // Create the user table
        String[] columnNames = {"Username", "Full Name", "Direct Friends Count", "Total Friends Count", "Creation Date"};
        Object[][] data = getUserTableData();

        // Setup the user table
        userTable = new JTable(new DefaultTableModel(data, columnNames));
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(userTable.getModel());
        userTable.setRowSorter(sorter);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        add(userScrollPane, BorderLayout.CENTER);

        // Create search bar
        searchField = new JTextField(15);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by name:"));
        searchPanel.add(searchField);
        add(searchPanel, BorderLayout.NORTH);

        // Create action buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        filterButton = new JButton("Filter by name");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(filterButton);

        filterButton = new JButton("Filter by direct friends");
        buttonPanel.add(filterButton);

        // Create panel for filtering by friends count
        filterFriendsField = new JTextField(5);
        filterComparisonField = new JTextField(5);
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by direct friends count (>=):"));
        filterPanel.add(filterFriendsField);
        filterPanel.add(new JLabel("Filter condition (greater than, less than, equal to):"));
        filterPanel.add(filterComparisonField);
        buttonPanel.add(filterPanel);

        // Add action buttons to panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Get table data from the list of users
    private Object[][] getUserTableData() {
        Object[][] data = new Object[users.size()][5];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getFullName();
            data[i][2] = user.getDirectFriends();
            data[i][3] = user.getTotalFriends();
            data[i][4] = user.getCreationDate();
        }
        return data;
    }

    // User class to store user information
    class User {
        private String username;
        private String fullName;
        private int directFriends;
        private int totalFriends;
        private String creationDate;

        public User(String username, String fullName, int directFriends, String creationDate) {
            this.username = username;
            this.fullName = fullName;
            this.directFriends = directFriends;
            this.totalFriends = directFriends * 2;  // Assuming total friends (including friends of friends) is twice the number of direct friends
            this.creationDate = creationDate;
        }

        public String getUsername() {
            return username;
        }

        public String getFullName() {
            return fullName;
        }

        public int getDirectFriends() {
            return directFriends;
        }

        public int getTotalFriends() {
            return totalFriends;
        }

        public String getCreationDate() {
            return creationDate;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("User Management");
        UserListApp userPanel = new UserListApp();
        frame.add(userPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
