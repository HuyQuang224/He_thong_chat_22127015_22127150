package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.friendController;
import controller.logInController;
import model.User;

import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FindUser extends JFrame {

    friendController friendController = new friendController();
    JTable usersTable;
    User user;

    public FindUser(User user) {
        setTitle("Find User");
        setSize(500, 600);
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

        this.user = user;

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

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
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        mainPanel.add(searchButton, gbc);

        // User table
        String[] columnNames = {"ID", "Username", "Fullname", "Action" }; // Include ID column
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Actions column is editable
            }
        };

        usersTable = new JTable(model);
        usersTable.getColumn("ID").setMinWidth(0);
        usersTable.getColumn("ID").setMaxWidth(0);
        usersTable.getColumn("ID").setWidth(0); // Hide ID column

        usersTable.setRowHeight(30);
        usersTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        usersTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(usersTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Users"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Create group button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createGroupButton = new JButton("Create Group");
        actionPanel.add(createGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(actionPanel, gbc);

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            List<User> users = friendController.searchUsers(searchText, user.getUsername(), user.getId());

            // Clear old rows
            DefaultTableModel model1 = (DefaultTableModel) usersTable.getModel();
            model1.setRowCount(0);

            // Add new rows
            for (User user1 : users) {
                model.addRow(new Object[]{user1.getId(), user1.getUsername(), user1.getFullName(), ""});
            }
        });

        createGroupButton.addActionListener(e -> {
            dispose();
            try {
                new GroupCreate(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            };
        });

        add(mainPanel);
    }

    // Custom cell renderer to display button in table
    static class ButtonRenderer extends JPanel implements TableCellRenderer {

        private final JButton chatButton = new JButton("Chat");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(chatButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor to handle click button in table
    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JPanel panel = new JPanel();
        private final JButton chatButton = new JButton("Chat");

        public ButtonEditor(JCheckBox checkBox, int userId) {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(chatButton);

            chatButton.addActionListener(e ->{
                int row = usersTable.getSelectedRow();
                int toUserId = (int) usersTable.getValueAt(row, 0);
                String toUsername = usersTable.getValueAt(row, 1).toString();
                dispose();
                try {
                    new ChatUI(user, toUserId, toUsername).setVisible(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                };
            });
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

