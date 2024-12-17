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

public class FriendList extends JFrame {
    List<User> friends;
    String filterName = "";

    public FriendList(User user) throws Exception {
        setTitle("Friend List");
        setSize(800, 600);
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

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back Button
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(backButton, gbc);

        JLabel filterLabel = new JLabel("Filter by name:");
        JTextField filterField = new JTextField(10);
        JButton filterButton = new JButton("Filter");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(filterLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(filterField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(filterButton, gbc);

        // Friend Table
        String[] columnNames = {"Username", "Fullname", "Status", "Actions", "ID"}; // Include ID column
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
        friendTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        friendTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(friendTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Friends"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Other action button
        JButton searchButton = new JButton("Search Friend");
        JButton findUserButton = new JButton("Find User");
        JButton viewOnlineFriendsButton = new JButton("Online Friends");
        JButton viewFriendRequestsButton = new JButton("Friend Requests");
        JButton viewBLockListButton = new JButton("Block List");
        
        // Search Friend button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchButton, gbc);

        // Find User button
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(findUserButton, gbc);

        // View Online Friends button
        gbc.gridx = 2;
        gbc.gridy = 3;
        mainPanel.add(viewOnlineFriendsButton, gbc);

        // View Friend Requests button
        gbc.gridx = 3;
        gbc.gridy = 3;
        mainPanel.add(viewFriendRequestsButton, gbc);

        // View Block List
        gbc.gridx = 4;
        gbc.gridy = 3;
        mainPanel.add(viewBLockListButton, gbc);

        friendController controller = new friendController();
        friends = controller.getFriendList(user.getId());

        filterButton.addActionListener(e -> {
            filterName = filterField.getText().trim();
            try {   
                friends = controller.getFilteredFriends(user.getId(), filterName);

                DefaultTableModel model1 = (DefaultTableModel) friendTable.getModel();
                model1.setRowCount(0); // Clear existing rows

                for (User friend : friends) {
                    model1.addRow(new Object[]{
                        friend.getUsername(),
                        friend.getFullName(),
                        friend.getStatus(),
                        "",
                        friend.getId()
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            // Load Friend List
            try {
                friends = controller.getFilteredFriends(user.getId(), filterName);
                friends = controller.updateStatus(friends);

                DefaultTableModel model1 = (DefaultTableModel) friendTable.getModel();
                model1.setRowCount(0);
                for (User friend : friends) {
                    model1.addRow(new Object[]{
                        friend.getUsername(),
                        friend.getFullName(),
                        friend.getStatus(),
                        "",
                        friend.getId()
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS); // Kiểm tra mỗi 2 giây


        backButton.addActionListener(e -> {
            dispose();
            new UserOption(user).setVisible(true);;
        });

        searchButton.addActionListener(e -> {
            dispose();
            new Search(user).setVisible(true);
        });

        findUserButton.addActionListener(e -> {
            dispose();
            new FindUser(user).setVisible(true);
        });

        viewOnlineFriendsButton.addActionListener(e -> {
            dispose();
            try {
                new OnlFriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        viewFriendRequestsButton.addActionListener(e -> {
            dispose();
            try {
                new RequestList(user).setVisible(true);;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        viewBLockListButton.addActionListener(e -> {
            dispose();
            try {
                new blockList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // Add Main Panel
        add(mainPanel);
    }


    // Custom Button Renderer
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton blockButton = new JButton("Block");
        private final JButton removeButton = new JButton("Remove");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(blockButton);
            add(removeButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Button Editor
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton blockButton = new JButton("Block");
        private final JButton removeButton = new JButton("Remove");
        private final int userId;
        private final friendController controller; // Add FriendController instance
    
        public ButtonEditor(JCheckBox checkBox, int userId) {
            this.userId = userId;
            this.controller = new friendController(); // Initialize FriendController
    
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(blockButton);
            panel.add(removeButton);
    
            blockButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
            
                if (row >= 0) { // Check to ensure the row exists
                    String friendName = (String) table.getValueAt(row, 0);
                    int friendId = (int) table.getValueAt(row, 4); // Assuming ID is in a hidden column
                    for (int i = 0; i < friends.size(); i++) {
                        if (friends.get(i).getId() == friendId){
                            friends.remove(i);
                        }
                    }
                    boolean success = false;
                    try {
                        success = controller.blockFriend(userId, friendId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            
                    if (success) {
                        System.out.println("Blocked friend: " + friendName);
                        stopCellEditing(); // Stop editing before modifying the model
                        ((DefaultTableModel) table.getModel()).removeRow(row); // Remove row from table
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to block " + friendName);
                    }
                }
            });
    
            removeButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
            
                if (row >= 0) { // Check to ensure the row exists
                    String friendName = (String) table.getValueAt(row, 0);
                    int friendId = (int) table.getValueAt(row, 4); // Assuming ID is in a hidden column
                    for (int i = 0; i < friends.size(); i++) {
                        if (friends.get(i).getId() == friendId){
                            friends.remove(i);
                        }
                    }

                    boolean success = false;
                    try {
                        success = controller.removeFriend(userId, friendId);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
            
                    if (success) {
                        System.out.println("Removed friend: " + friendName);
                        stopCellEditing(); // Stop editing before modifying the model
                        ((DefaultTableModel) table.getModel()).removeRow(row); // Remove row from table
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to remove " + friendName);
                    }
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
