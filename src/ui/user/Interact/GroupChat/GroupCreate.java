import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupCreate extends JFrame {
    private JTextField groupNameField, addMemberField;
    private JTable membersTable;
    private JButton addMemberButton, removeMemberButton, createButton;

    public GroupCreate() {
        setTitle("Group Chat Creation");
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
        String[] columnNames = {"Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        membersTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(membersTable);
        memberManagementPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add/Remove Members Panel
        JPanel addRemoveMemberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRemoveMemberPanel.add(new JLabel("Add/Remove Member:"));
        addMemberField = new JTextField(15);
        addRemoveMemberPanel.add(addMemberField);
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

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GroupCreate::new);
    }
}
