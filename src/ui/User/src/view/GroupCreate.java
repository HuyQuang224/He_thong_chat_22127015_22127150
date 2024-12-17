package view;

import model.User;
import model.groupChat;
import controller.friendController;
import controller.logInController;
import controller.chatController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

public class GroupCreate extends JFrame {
    chatController chatController;
    friendController FriendController;
    private JTextField groupNameField;
    private JTable membersTable;
    private JButton addMemberButton, removeMemberButton, createButton;
    private DefaultTableModel membersTableModel;
    private List<User> availableFriends;  // Assume this is the list of the user's friends from the database
    DefaultListModel<String> listModel;

    public GroupCreate(User user) throws Exception {
        setTitle("Group Chat Creation");
        setSize(600, 750);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thêm WindowListener để xử lý trạng thái khi cửa sổ đóng
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    new FriendList(user).setVisible(true);;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Group Creation Panel
        JPanel createGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createGroupPanel.setBorder(BorderFactory.createTitledBorder("Create Group"));
        createGroupPanel.add(new JLabel("Group Name:"));
        groupNameField = new JTextField(20);
        createGroupPanel.add(groupNameField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        mainPanel.add(createGroupPanel, gbc);

        // Member Management Panel
        JPanel memberManagementPanel = new JPanel(new BorderLayout());
        memberManagementPanel.setBorder(BorderFactory.createTitledBorder("Manage Members"));

        // Members Table
        String[] columnNames = {"ID", "Username", "Role"};
        membersTableModel = new DefaultTableModel(columnNames, 0);
        membersTable = new JTable(membersTableModel);

        membersTable.getColumn("ID").setMinWidth(0);
        membersTable.getColumn("ID").setMaxWidth(0);
        membersTable.getColumn("ID").setWidth(0); // Hide ID column

        JScrollPane tableScrollPane = new JScrollPane(membersTable);
        memberManagementPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add/Remove Members Panel
        JPanel addRemoveMemberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addMemberButton = new JButton("Add");
        addRemoveMemberPanel.add(addMemberButton);

        removeMemberButton = new JButton("Remove");
        addRemoveMemberPanel.add(removeMemberButton);

        memberManagementPanel.add(addRemoveMemberPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        mainPanel.add(memberManagementPanel, gbc);

        // Create Button Panel
        JPanel createButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createButton = new JButton("Create Group");
        createButtonPanel.add(createButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(createButtonPanel, gbc);

        add(mainPanel);

        FriendController = new friendController();

        // Retrieve available friends from the database
        availableFriends = FriendController.getFriendList(user.getId());

        // Event Handling
        addMemberButton.addActionListener(e -> {
            try {
                showAddFriendDialog(user);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        removeMemberButton.addActionListener(e -> removeMemberFromGroup());
        createButton.addActionListener(e -> {
            try {
                createGroup(user);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    // Show dialog to select a friend to add
    private void showAddFriendDialog(User user) throws Exception {
        
        JDialog addFriendsDialog = new JDialog(this, "Select Friends", true);
        addFriendsDialog.setSize(400, 300);
        addFriendsDialog.setLayout(new BorderLayout());

        // Create the DefaultListModel and populate it
        listModel = new DefaultListModel<>();
        for (User friend : availableFriends) {
            listModel.addElement(friend.getUsername());
        }

        // Create the JList and add it to a JScrollPane
        JList<String> friendsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(friendsList);
        addFriendsDialog.add(scrollPane, BorderLayout.CENTER);

        // Add a horizontal separator
        JSeparator separator = new JSeparator();
        addFriendsDialog.add(separator, BorderLayout.NORTH);

        // Add the "Add" button to the bottom
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String selectedFriendUsername = friendsList.getSelectedValue();
            if (selectedFriendUsername != null) {
                addMemberToTable(selectedFriendUsername);
                // Remove the selected friend from the list
                listModel.removeElement(selectedFriendUsername);
                // Also remove the friend from the availableFriends list
                availableFriends.removeIf(friend -> friend.getUsername().equals(selectedFriendUsername));
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        addFriendsDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Center the dialog on the screen
        addFriendsDialog.setLocationRelativeTo(null);
        addFriendsDialog.setVisible(true);
}

    // Add selected friend to the table
    private void addMemberToTable(String friendUsername) {
        // Find the user object of the selected friend
        User selectedFriend = availableFriends.stream()
                                            .filter(friend -> friend.getUsername().equals(friendUsername))
                                            .findFirst()
                                            .orElse(null);
        if (selectedFriend != null) {
            membersTableModel.addRow(new Object[]{selectedFriend.getId(), selectedFriend.getUsername(), "Member"});
        }
    }

    // Remove selected member from the table
    private void removeMemberFromGroup() {
        int selectedRow = membersTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Retrieve the username of the selected row
            int removedFriendId = (int) membersTableModel.getValueAt(selectedRow, 0);
            String removedFriendUsername = (String) membersTableModel.getValueAt(selectedRow, 1);
            membersTableModel.removeRow(selectedRow);

            // Find the User object for the removed friend
            User removedFriend = availableFriends.stream()
                                                .filter(friend -> friend.getUsername().equals(removedFriendUsername))
                                                .findFirst()
                                                .orElse(null);

            if (removedFriend == null) {
                // If not found in availableFriends, recreate the user object
                removedFriend = new User();
                removedFriend.setId(removedFriendId);
                removedFriend.setUsername(removedFriendUsername);
                // Add the removed friend back to the available friends list
                availableFriends.add(removedFriend);
            }
            listModel.addElement(removedFriendUsername);
        }
    }

    void createGroup(User user) throws SQLException, Exception {
        String groupName = groupNameField.getText().trim();
        if (groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Group name is required.");
            return;
        }

        if (membersTableModel.getRowCount() == 0){
            JOptionPane.showMessageDialog(this, "Add at least one member.");
            return;
        }

        chatController = new chatController();
        groupChat groupChat = chatController.createGroupChat(groupName);
        chatController.insertMember(groupChat.getId(), user.getId(), "admin");

        for (int i = 0; i < membersTableModel.getRowCount(); i++) {
            int memberId = (int) membersTableModel.getValueAt(i, 0);
            chatController.insertMember(groupChat.getId(), memberId, "member");
        }

        dispose();
        new groupChatList(user).setVisible(true);
    }
}