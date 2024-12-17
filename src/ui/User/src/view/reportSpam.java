package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import controller.chatController;
import controller.logInController;
import model.User;
import model.Message;

import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class reportSpam extends JFrame {

    chatController chatController = new chatController();
    JTable messTable;
    User user;

    public reportSpam(User user, int toUserId, String toUsername) throws Exception {
        setTitle("Report Spam");
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
        String[] columnNames = {"toUserId", "messageId", "Message", "Action" };
        Object[][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only Actions column is editable
            }
        };

        messTable = new JTable(model);
        messTable.getColumn("toUserId").setMinWidth(0);
        messTable.getColumn("toUserId").setMaxWidth(0);
        messTable.getColumn("toUserId").setWidth(0);

        messTable.getColumn("messageId").setMinWidth(0);
        messTable.getColumn("messageId").setMaxWidth(0);
        messTable.getColumn("messageId").setWidth(0);

        messTable.setRowHeight(30);
        messTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        messTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(messTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Users"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        chatController = new chatController();
        loadMessage(chatController.loadfromUserMessage(user.getId(), toUserId));


        backButton.addActionListener(e -> {
            dispose();
            try {
                new ChatUI(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            };
        });

        add(mainPanel);
    }

    public void loadMessage(List<Message> messages) {
        // Clear old rows
        DefaultTableModel model1 = (DefaultTableModel) messTable.getModel();
        model1.setRowCount(0);

        // Add new rows
        for (Message message : messages) {
            model1.addRow(new Object[]{message.getFromUser(), message.getId(), message.getContent(), ""});
        }
    }

    // Custom cell renderer to display button in table
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton reportButton = new JButton("Report");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(reportButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor to handle click button in table
    public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton reportButton = new JButton("Report");
        private int userId;

        public ButtonEditor(JCheckBox checkBox, int userId) {
            this.userId = userId;
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(reportButton);

            reportButton.addActionListener(e -> {
                int row = messTable.getSelectedRow();
                int reportedUserId = (int) messTable.getValueAt(row, 0);
                int messageId = (int) messTable.getValueAt(row, 1);

                // Hiển thị hộp thoại để nhập lý do báo cáo
                String reason = JOptionPane.showInputDialog(panel, "Enter report reason:", "Report Spam", JOptionPane.PLAIN_MESSAGE);

                if (reason != null && !reason.trim().isEmpty()) {
                    // Lưu thông tin báo cáo vào cơ sở dữ liệu
                    try {
                        chatController.reportSpam(userId, reportedUserId, messageId, reason);
                        JOptionPane.showMessageDialog(panel, "Báo cáo thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Có lỗi xảy ra khi báo cáo!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

