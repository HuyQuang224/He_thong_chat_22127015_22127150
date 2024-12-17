package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.friendController;
import controller.logInController;
import controller.chatController;
import model.Message;
import model.User;
import model.groupChat;

import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class groupChatList extends JFrame {

    friendController friendController = new friendController();
    JTable groupsTable;
    User user;

    public groupChatList(User user) throws Exception {
        setTitle("Group Chat List");
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

        // User table
        String[] columnNames = {"ID", "Group Name", "Action"}; // Include ID column
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only Actions column is editable
            }
        };

        groupsTable = new JTable(model);
        groupsTable.getColumn("ID").setMinWidth(0);
        groupsTable.getColumn("ID").setMaxWidth(0);
        groupsTable.getColumn("ID").setWidth(0); // Hide ID column

        groupsTable.setRowHeight(30);
        groupsTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        groupsTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(groupsTable);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);
        
        chatController chatController = new chatController();
        List<groupChat> groupChats = chatController.getGroupChats(user.getId());
        loadGroupChat(groupChats);

        backButton.addActionListener(e -> {
            dispose();
            try {
                new UserOption(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            };
        });

        add(mainPanel);
    }

    public void loadGroupChat(List<groupChat> groupChats) {
        // Clear old rows
        DefaultTableModel model1 = (DefaultTableModel) groupsTable.getModel();
        model1.setRowCount(0);

        // Add new rows
        for (groupChat groupChat : groupChats) {
            model1.addRow(new Object[]{groupChat.getId(), groupChat.getName(), ""});
        }
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
                int row = groupsTable.getSelectedRow();
                int toUserId = (int) groupsTable.getValueAt(row, 0);
                String toUsername = groupsTable.getValueAt(row, 1).toString();
                dispose();
                try {
                    new GroupChatUI(user, toUserId, toUsername).setVisible(true);
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


