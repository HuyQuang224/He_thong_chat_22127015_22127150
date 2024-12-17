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

public class OnlFriendList extends JFrame {
    User user;
    List<User> friends;
    String filterName = "";

    public OnlFriendList(User user) throws Exception {
        setTitle("Online Friends List");
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

        // Online Friend table
        String[] columnNames = {"ID" ,"Username", "Fullname","Actions"};
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        JTable onlineFriendsTable = new JTable(model);
        onlineFriendsTable.getColumn("ID").setMinWidth(0);
        onlineFriendsTable.getColumn("ID").setMaxWidth(0);
        onlineFriendsTable.getColumn("ID").setWidth(0); // Hide ID column

        onlineFriendsTable.setRowHeight(30);
        onlineFriendsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        onlineFriendsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));


        JScrollPane tableScrollPane = new JScrollPane(onlineFriendsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Online Friends"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        // Create Group button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createGroupButton = new JButton("Create Group");
        actionPanel.add(createGroupButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(actionPanel, gbc);

        friendController controller = new friendController();
        friends = controller.getFilteredOnlineFriends(user.getId(), "");

        filterButton.addActionListener(e -> {
            filterName = filterField.getText().trim();
            try {   
                friends = controller.getFilteredOnlineFriends(user.getId(), filterName);

                DefaultTableModel model1 = (DefaultTableModel) onlineFriendsTable.getModel();
                model1.setRowCount(0); // Clear existing rows

                for (User friend : friends) {
                    model1.addRow(new Object[]{
                        friend.getId(),
                        friend.getUsername(),
                        friend.getFullName(),
                        ""
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        createGroupButton.addActionListener(e -> {
            dispose();
            try {
                new GroupCreate(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            };
        });

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            // Load Friend List
            try {
                friends = controller.getFilteredOnlineFriends(user.getId(), filterName);

                DefaultTableModel model1 = (DefaultTableModel) onlineFriendsTable.getModel();
                model1.setRowCount(0);
                for (User friend : friends) {  
                    model1.addRow(new Object[]{
                        friend.getId(),
                        friend.getUsername(),
                        friend.getFullName(),
                        ""
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS); // Kiểm tra mỗi 2 giây

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

    // Custom Button Renderer
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton chatButton = new JButton("Chat");
        private final JButton blockButton = new JButton("Block");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(chatButton);
            add(blockButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom Button Editor
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton chatButton = new JButton("Chat");
        private final JButton blockButton = new JButton("Block");
        private final int userId;
        private final friendController controller; // Add FriendController instance
    
        public ButtonEditor(JCheckBox checkBox, int userId) {
            this.userId = userId;
            this.controller = new friendController(); // Initialize FriendController
    
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(chatButton);
            panel.add(blockButton);
    
            blockButton.addActionListener(e -> {
                JTable table = (JTable) panel.getParent();
                int row = table.getEditingRow();
            
                if (row >= 0) { // Check to ensure the row exists
                    String friendName = table.getValueAt(row, 1).toString();
                    int friendId = (int) table.getValueAt(row, 0); // Assuming ID is in a hidden column
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
    
            chatButton.addActionListener(e ->{
                JTable table = (JTable) panel.getParent();
                int row = table.getSelectedRow();
                int toUserId = (int) table.getValueAt(row, 0);
                String toUsername = table.getValueAt(row, 1).toString();
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
