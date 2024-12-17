package view;

import controller.friendController;
import controller.logInController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Search extends JFrame {
    List<User> users;
    String searchText;

    public Search(User user) {
        friendController friendController = new friendController();

        setTitle("Search Friends");
        setSize(700, 600);
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

        // Back button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(backButton, gbc);

        // Search bar
        JLabel searchLabel = new JLabel("Search Friend:");
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(searchButton, gbc);

        // Table to display search results
        String[] columnNames = {"ID", "Username", "Fullname", "Action" }; // Include ID column
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Actions column is editable
            }
        };
        JTable friendTable = new JTable(model);
        friendTable.getColumn("ID").setMinWidth(0);
        friendTable.getColumn("ID").setMaxWidth(0);
        friendTable.getColumn("ID").setWidth(0); // Hide ID column

        friendTable.setRowHeight(30);
        friendTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        friendTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), friendController, user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(friendTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Search"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Search button event
        searchButton.addActionListener(e -> {
            searchText = searchField.getText().trim();
            users = friendController.searchFriends(searchText, user.getUsername(), user.getId());

            // Clear old rows
            DefaultTableModel model1 = (DefaultTableModel) friendTable.getModel();
            model1.setRowCount(0);

            // Add new rows
            for (User user1 : users) {
                model.addRow(new Object[]{user1.getId(), user1.getUsername(), user1.getFullName(), "Add Friend"});
            }
        });

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            // Load Friend List
            try {
                users = friendController.searchFriends(searchText, user.getUsername(), user.getId());

                DefaultTableModel model1 = (DefaultTableModel) friendTable.getModel();
                model1.setRowCount(0);
                for (User user1 : users) {
                    model.addRow(new Object[]{user1.getId(), user1.getUsername(), user1.getFullName(), "Add Friend"});
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS); // Kiểm tra mỗi 2 giây

        // Back button event
        backButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // Add the main panel to the frame
        add(mainPanel);
    }

    // Renderer for the action buttons in the table
    public class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton addButton = new JButton("Add Friend");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(addButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor for the action buttons in the table
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton addButton = new JButton("Add Friend");
        private final int currentUserId;
        private final friendController friendcontroller; // Add FriendController instance

        public ButtonEditor(JCheckBox checkBox, friendController friendController, int currentUserId) {
            this.currentUserId = currentUserId;
            this.friendcontroller = new friendController(); // Initialize FriendController
    
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(addButton);

            addButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();

                int userId = (int) table.getValueAt(row, 0); // Get user ID from the table
                boolean success = false;
                try {
                    success = friendController.sendFriendRequest(currentUserId, userId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (success) {
                    JOptionPane.showMessageDialog(null, "Friend request sent successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Already sent or pending.");
                }
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