package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupCreate extends JFrame {
    private JTextField groupNameField, addMemberField;
    private JTable membersTable;
    private JButton addMemberButton, removeMemberButton, createButton;

    public GroupCreate() {
        setTitle("Group Chat Creation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Group Creation Panel
        JPanel createGroupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createGroupPanel.setBorder(BorderFactory.createTitledBorder("Create Group"));
        createGroupPanel.add(new JLabel("Group Name:"));
        groupNameField = new JTextField(15);
        createGroupPanel.add(groupNameField);
        
        mainPanel.add(createGroupPanel);

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
        addRemoveMemberPanel.add(new JLabel("Add/Remove Member:"));
        addMemberField = new JTextField(10);
        addRemoveMemberPanel.add(addMemberField);
        addMemberButton = new JButton("Add");
        addRemoveMemberPanel.add(addMemberButton);

        removeMemberButton = new JButton("Remove");
        addRemoveMemberPanel.add(removeMemberButton);

        memberManagementPanel.add(addRemoveMemberPanel, BorderLayout.SOUTH);

        mainPanel.add(memberManagementPanel);

        // Create Button Panel
        JPanel CreateButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createButton = new JButton("Create Group");
        CreateButtonPanel.add(createButton);
        mainPanel.add(CreateButtonPanel);
        // Add main panel to frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GroupCreate frame = new GroupCreate();
            frame.setVisible(true);
        });
    }
}

