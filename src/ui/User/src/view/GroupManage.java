package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.chatController;
import controller.friendController;
import controller.logInController;

import java.awt.*;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.User;
import model.groupChat;

public class GroupManage extends JFrame {
    private JTextField groupNameField;
    private JTable membersTable;
    private JButton renameGroupButton, addMemberButton, setAdminButton, removeMemberButton;
    DefaultTableModel membersTableModel;
    chatController chatController;
    friendController FriendController;
    private List<User> availableFriends;  // Assume this is the list of the user's friends from the database
    DefaultListModel<String> listModel;
    String groupName1;

    public GroupManage(User user, int groupId, String groupName) throws Exception {
        setTitle("Group Chat Management");
        setSize(600, 750);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thêm WindowListener để xử lý trạng thái khi cửa sổ đóng
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cập nhật trạng thái Offline khi đóng cửa sổ
                logInController logInController = new logInController();
                try {
                    logInController.updateStatus(user.getId(), "Offline");
                    System.exit(0); // Thoát chương trình
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(backButton, gbc);

        // Rename Group Panel
        JPanel renameGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        renameGroupPanel.setBorder(BorderFactory.createTitledBorder("Rename Group"));
        renameGroupPanel.add(new JLabel("New Group Name:"));
        groupNameField = new JTextField(20);
        renameGroupPanel.add(groupNameField);
        renameGroupButton = new JButton("Rename Group");
        renameGroupPanel.add(renameGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        mainPanel.add(renameGroupPanel, gbc);

        // Member Management Panel
        JPanel memberManagementPanel = new JPanel(new BorderLayout());
        memberManagementPanel.setBorder(BorderFactory.createTitledBorder("Manage Members"));

        // Members Table
        String[] columnNames = {"ID", "Username", "Role"};
        membersTableModel = new DefaultTableModel(columnNames, 0);
        membersTable = new JTable(membersTableModel);

        JScrollPane scrollPane = new JScrollPane(membersTable);
        memberManagementPanel.add(scrollPane, BorderLayout.CENTER);

        membersTable.getColumn("ID").setMinWidth(0);
        membersTable.getColumn("ID").setMaxWidth(0);
        membersTable.getColumn("ID").setWidth(0); // Hide ID column

        // Add/Remove Members Panel
        JPanel addRemoveMemberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setAdminButton = new JButton("Set as Admin");
        addRemoveMemberPanel.add(setAdminButton);

        addMemberButton = new JButton("Add");
        addRemoveMemberPanel.add(addMemberButton);

        removeMemberButton = new JButton("Remove");
        addRemoveMemberPanel.add(removeMemberButton);

        memberManagementPanel.add(addRemoveMemberPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(memberManagementPanel, gbc);

        add(mainPanel);

        this.groupName1 = groupName;
        FriendController = new friendController();
        chatController = new chatController();

        // Retrieve available friends from the database
        availableFriends = FriendController.getFriendsInGroup(user.getId(), groupId);
        loadGroupMem(availableFriends);

        renameGroupButton.addActionListener(e -> {
            String newGroupName = groupNameField.getText().trim();
            if (newGroupName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Group name cannot be empty!", 
                                              "Invalid Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            else {
                chatController.renameGroup(groupId, newGroupName);
                groupName1 = newGroupName;
                JOptionPane.showMessageDialog(this, "Group renamed successfully!", 
                                      "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new GroupChatUI(user, groupId, groupName1).setVisible(true);;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public void loadGroupMem(List<User> members) {
        // Clear old rows
        DefaultTableModel model1 = (DefaultTableModel) membersTable.getModel();
        model1.setRowCount(0);

        // Add new rows
        for (User member : members) {
            model1.addRow(new Object[]{member.getId(), member.getUsername(), member.getRole()});
        }
    }

    // Custom cell renderer to display button in table
    static class ButtonRenderer extends JPanel implements TableCellRenderer {

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor to handle click button in table
    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();

        public ButtonEditor(JCheckBox checkBox, int userId) {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
