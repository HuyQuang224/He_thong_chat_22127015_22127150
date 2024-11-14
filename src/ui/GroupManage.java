package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupManage extends JFrame {
    private JTextField groupNameField, addMemberField;
    private JTable membersTable;
    private JButton renameGroupButton, addMemberButton, setAdminButton, removeMemberButton, chatButton, encryptGroupButton;

    public GroupManage() {
        setTitle("Group Chat Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Rename Group Panel
        JPanel renameGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        renameGroupPanel.setBorder(BorderFactory.createTitledBorder("Rename Group"));
        renameGroupPanel.add(new JLabel("New Group Name:"));
        groupNameField = new JTextField(15);
        renameGroupPanel.add(groupNameField);
        renameGroupButton = new JButton("Rename Group");
        renameGroupPanel.add(renameGroupButton);
        mainPanel.add(renameGroupPanel);

        // Member Management Panel
        JPanel memberManagementPanel = new JPanel();
        memberManagementPanel.setBorder(BorderFactory.createTitledBorder("Manage Members"));
        memberManagementPanel.setLayout(new BorderLayout());

        // Members Table
        String[] columnNames = {"Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        membersTable = new JTable(model);
        memberManagementPanel.add(new JScrollPane(membersTable), BorderLayout.CENTER);

        // Add/Remove Members Panel
        JPanel addRemoveMemberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRemoveMemberPanel.add(new JLabel("Add Member:"));
        addMemberField = new JTextField(10);
        addRemoveMemberPanel.add(addMemberField);
        addMemberButton = new JButton("Add");
        addRemoveMemberPanel.add(addMemberButton);

        setAdminButton = new JButton("Set as Admin");
        addRemoveMemberPanel.add(setAdminButton);

        removeMemberButton = new JButton("Remove");
        addRemoveMemberPanel.add(removeMemberButton);

        memberManagementPanel.add(addRemoveMemberPanel, BorderLayout.SOUTH);

        mainPanel.add(memberManagementPanel);

        // Chat and Encrypt Panel
        JPanel chatEncryptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chatEncryptPanel.setBorder(BorderFactory.createTitledBorder("Chat Options"));
        chatButton = new JButton("Open Chat");
        chatEncryptPanel.add(chatButton);
        encryptGroupButton = new JButton("Encrypt Group");
        chatEncryptPanel.add(encryptGroupButton);
        mainPanel.add(chatEncryptPanel);

        // Add main panel to frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GroupManage frame = new GroupManage();
            frame.setVisible(true);
        });
    }
}

