package view;

import javax.lang.model.type.NullType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import view.Search.ButtonRenderer;
import controller.chatController;
import controller.logInController;
import model.Message;
import model.User;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.*;

public class ChatHistory extends JFrame {
    chatController chatController;
    List<Message> Messages;
    JTable messTable;
    int toUserId;
    User user;
    String toUsername;

    public ChatHistory(User user, int toUserId, String toUsername) throws Exception {
        setTitle("Chat History");
        setSize(600, 500);
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
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Top panel for buttons and status label (back button, delete buttons)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Button panel for the top buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Back button
        JButton backButton = new JButton("Back");
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(backButton);
        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Delete all button
        JButton deleteAllButton = new JButton("Delete All Chat History");
        deleteAllButton.setBackground(Color.RED);
        deleteAllButton.setForeground(Color.WHITE);

        // Delete selected button
        JButton deleteSelectedButton = new JButton("Delete Selected Messages");
        deleteSelectedButton.setBackground(Color.RED);
        deleteSelectedButton.setForeground(Color.WHITE);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(deleteSelectedButton);
        rightButtonPanel.add(deleteAllButton);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Add button panel to the top panel
        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add top panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(topPanel, gbc);

        // User table
        String[] columnNames = {"fromUserId", "messageId", "Message", "Visible"};
        Object[][] data = {};

        // Lớp DefaultTableModel tùy chỉnh để không cho phép chỉnh sửa
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };

        messTable = new JTable(model);
        messTable.getColumn("fromUserId").setMinWidth(0);
        messTable.getColumn("fromUserId").setMaxWidth(0);
        messTable.getColumn("fromUserId").setWidth(0);

        messTable.getColumn("messageId").setMinWidth(0);
        messTable.getColumn("messageId").setMaxWidth(0);
        messTable.getColumn("messageId").setWidth(0);

        messTable.getColumn("Visible").setMinWidth(0);
        messTable.getColumn("Visible").setMaxWidth(0);
        messTable.getColumn("Visible").setWidth(0);

        JScrollPane tableScrollPane = new JScrollPane(messTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("History"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);


        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 1, 5, 5));

        // SearchChat
        JPanel searchChatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchChatLabel = new JLabel("Search in chat:");
        JTextField searchChatField = new JTextField(20);
        JButton searchChatButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        searchChatPanel.add(searchChatLabel);
        searchChatPanel.add(searchChatField);
        searchChatPanel.add(searchChatButton);
        searchChatPanel.add(resetButton);

        // Add search chat panel to search panel
        searchPanel.add(searchChatPanel);

        // Add search panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        chatController = new chatController();
        try {
            Messages = chatController.loadMessage(user.getId(), toUserId);
            loadMessage(Messages);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.toUserId = toUserId;
        this.user = user;
        this.toUsername = toUsername;

        deleteAllButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete all messages?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    for (int row = 0; row < messTable.getRowCount(); row++) {
                        int messageId = (int) messTable.getValueAt(row, 1);
                        int fromUserId = (int) messTable.getValueAt(row, 0);
                        int visibleId = (int) messTable.getValueAt(row, 3);
                        if (visibleId != 0) {
                            chatController.deleteMessageForEveryone(messageId);
                        }
                        else {
                            chatController.deleteMessageForUser(messageId, user.getId());
                        }
                        
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    Messages = chatController.loadMessage(user.getId(), toUserId);
                    loadMessage(Messages);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        deleteSelectedButton.addActionListener(e -> {
            int[] selectedRows = messTable.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "No messages selected!");
                return;
            }
        
            for (int row : selectedRows) {
                int messageId = (int) messTable.getValueAt(row, 1);
                int fromUserId = (int) messTable.getValueAt(row, 0);
                int visibleId = (int) messTable.getValueAt(row, 3);
        
                if (fromUserId == user.getId()) { // Own message
                    Object[] options = {"Delete for Me", "Delete for Everyone", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(
                            this,
                            "Choose deletion type:",
                            "Delete Message",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]);
        
                    if (choice == 0) {
                        try {
                            if (visibleId != 0) {
                                chatController.deleteMessageForEveryone(messageId);
                            }
                            else {
                                chatController.deleteMessageForUser(messageId, user.getId());
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else if (choice == 1) {
                        try {
                            chatController.deleteMessageForEveryone(messageId);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    else if (choice == 2) {
                        return;
                    }
                } else { // Other user's message
                    try {
                        chatController.deleteMessageForUser(messageId, user.getId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            try {
                Messages = chatController.loadMessage(user.getId(), toUserId);
                loadMessage(Messages);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        messTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Nhấn đúp chuột
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Lấy dòng được chọn
                    if (row != -1) {
                        int messageId = (int) target.getValueAt(row, 1); // Lấy messageId từ bảng
                        // String content = target.getValueAt(row, 2).toString(); // Lấy messageId từ bảng
                        for(int i = 0; i < Messages.size(); i++){
                            if(messageId == Messages.get(i).getId()) {
                                openChatUIAndScrollToMessage(messageId); // Hàm mở ChatUI và cuộn tới tin nhắn
                            }
                        }
                    }
                }
            }
        });

        searchChatButton.addActionListener(e -> {
            String searchText = searchChatField.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter text to search!");
                return;
            }
        
            try {
                Messages = chatController.searchMessages(user.getId(), toUserId, searchText);
                    loadMessage(Messages);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        resetButton.addActionListener(e -> {
            try {
                Messages = chatController.loadMessage(user.getId(), toUserId);
                loadMessage(Messages);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new ChatUI(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        add(mainPanel);
    }

    public void openChatUIAndScrollToMessage(int messageId) {
        try {
            // Mở giao diện ChatUI
            dispose();
            ChatUI chatUI = new ChatUI(user, toUserId, toUsername, messageId); // Truyền thêm messageId
            chatUI.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to open chat window.");
        }
    }

    public void loadMessage(List<Message> messages) {
        // Clear old rows
        DefaultTableModel model1 = (DefaultTableModel) messTable.getModel();
        model1.setRowCount(0);

        // Add new rows
        for (Message message : messages) {
            model1.addRow(new Object[]{message.getFromUser(), message.getId(), message.getContent(), message.getVisibleOnly()});
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
        private int userId;

        public ButtonEditor(JCheckBox checkBox, int userId) {
            this.userId = userId;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

            
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
