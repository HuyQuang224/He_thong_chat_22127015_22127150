package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import view.Search.ButtonRenderer;
import controller.chatController;
import controller.logInController;
import model.Message;
import model.User;
import model.groupChat;
import model.groupMessage;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.*;

public class GroupChatHistory extends JFrame {
    chatController chatController;
    List<groupMessage> Messages;
    JTable messTable;
    int groupId;
    User user;
    String groupName;

    public GroupChatHistory(User user, int groupId, String groupName) throws Exception {
        setTitle("Group Chat History");
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
        String[] columnNames = {"fromUserId", "messageId", "Message"};
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

        

        JScrollPane tableScrollPane = new JScrollPane(messTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("History"));

        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        chatController = new chatController();
        try {
            Messages = chatController.loadGroupMessages(groupId);
            loadMessage(Messages);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.groupId = groupId;
        this.user = user;
        this.groupName = groupName;

        messTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Nhấn đúp chuột
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // Lấy dòng được chọn
                    if (row != -1) {
                        int messageId = (int) target.getValueAt(row, 1); // Lấy messageId từ bảng
                        for(int i = 0; i < Messages.size(); i++){
                            if(messageId == Messages.get(i).getId()) {
                                openGroupChatUIAndScrollToMessage(messageId); // Hàm mở ChatUI và cuộn tới tin nhắn
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
                Messages = chatController.searchGroupMessages(groupId, searchText);
                loadMessage(Messages);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        resetButton.addActionListener(e -> {
            try {
                Messages = chatController.loadGroupMessages(groupId);
                loadMessage(Messages);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new GroupChatUI(user, groupId, groupName).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        add(mainPanel);
    }

    public void openGroupChatUIAndScrollToMessage(int messageId) {
        try {
            // Mở giao diện GroupChatUI
            dispose();
            GroupChatUI groupChatUI = new GroupChatUI(user, groupId, groupName, messageId); // Truyền thêm messageId
            groupChatUI.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unable to open chat window.");
        }
    }

    public void loadMessage(List<groupMessage> messages) {
        // Clear old rows
        DefaultTableModel model1 = (DefaultTableModel) messTable.getModel();
        model1.setRowCount(0);

        // Add new rows
        for (groupMessage message : messages) {
            model1.addRow(new Object[]{message.getFromUserId(), message.getId(), message.getContent()});
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
