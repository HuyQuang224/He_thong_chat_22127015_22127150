import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FriendList extends JFrame {

    public FriendList() {
        setTitle("Friend List");
        setSize(700, 600);
        setResizable(false); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Back button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(backButton, gbc);

        // Search with name
        JLabel searchLabel = new JLabel("Search Friend:");
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(searchButton, gbc);

        // Friend table
        String[] columnNames = {"User Name", "Status", "Friend Status"};
        Object[][] data = {
        };

        JTable friendTable = new JTable(new DefaultTableModel(data, columnNames));
        friendTable.setFillsViewportHeight(true); // Table fill viewport height
        JScrollPane tableScrollPane = new JScrollPane(friendTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Friends"));

        // Chat button and Create group button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Other action button
        JButton removeFriendButton = new JButton("Remove Friend");
        JButton blockFriendButton = new JButton("Block");
        JButton viewOnlineFriendsButton = new JButton("View Online Friends");
        JButton viewFriendRequestsButton = new JButton("View Friend Requests");
        JButton findUserButton = new JButton("Find User");

        // Remove Friend button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(removeFriendButton, gbc);

        // Block Friend button
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(blockFriendButton, gbc);

        // View Online Friends button
        gbc.gridx = 2;
        gbc.gridy = 3;
        mainPanel.add(viewOnlineFriendsButton, gbc);

        // View Friend Requests button
        gbc.gridx = 3;
        gbc.gridy = 3;
        mainPanel.add(viewFriendRequestsButton, gbc);

        // Find User button
        gbc.gridx = 4;
        gbc.gridy = 3;
        mainPanel.add(findUserButton, gbc);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FriendList::new);
    }
}
