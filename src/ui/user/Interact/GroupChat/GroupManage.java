import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupManage extends JFrame {
    private JTextField groupNameField, addMemberField;
    private JTable membersTable;
    private JButton renameGroupButton, addMemberButton, setAdminButton, removeMemberButton, chatButton, encryptGroupButton;

    public GroupManage() {
        setTitle("Group Chat Management");
        setSize(600, 750);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Rename Group Panel
        JPanel renameGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        renameGroupPanel.setBorder(BorderFactory.createTitledBorder("Rename Group"));
        renameGroupPanel.add(new JLabel("New Group Name:"));
        groupNameField = new JTextField(20);
        renameGroupPanel.add(groupNameField);
        renameGroupButton = new JButton("Rename Group");
        renameGroupPanel.add(renameGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        mainPanel.add(renameGroupPanel, gbc);

        // Member Management Panel
        JPanel memberManagementPanel = new JPanel(new BorderLayout());
        memberManagementPanel.setBorder(BorderFactory.createTitledBorder("Manage Members"));

        // Members Table
        String[] columnNames = {"Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        membersTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(membersTable);
        memberManagementPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add/Remove Members Panel
        JPanel addRemoveMemberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRemoveMemberPanel.add(new JLabel("Add Member:"));
        addMemberField = new JTextField(15);
        addRemoveMemberPanel.add(addMemberField);
        addMemberButton = new JButton("Add");
        addRemoveMemberPanel.add(addMemberButton);

        setAdminButton = new JButton("Set as Admin");
        addRemoveMemberPanel.add(setAdminButton);

        removeMemberButton = new JButton("Remove");
        addRemoveMemberPanel.add(removeMemberButton);

        memberManagementPanel.add(addRemoveMemberPanel, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(memberManagementPanel, gbc);

        // Chat and Encrypt Panel
        JPanel chatEncryptPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chatEncryptPanel.setBorder(BorderFactory.createTitledBorder("Chat Options"));
        chatButton = new JButton("Open Chat");
        chatEncryptPanel.add(chatButton);
        encryptGroupButton = new JButton("Encrypt Group");
        chatEncryptPanel.add(encryptGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(chatEncryptPanel, gbc);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GroupManage::new);
    }
}
