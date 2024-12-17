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

public class blockList extends JFrame {
    private friendController blockController;
    private DefaultTableModel model;
    private JTable blockTable;

    public blockList(User user) throws Exception {
        blockController = new friendController();

        setTitle("Block List");
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

        // Friend Request table
        String[] columnNames = {"ID", "Username", "Actions"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cột Actions có thể chỉnh sửa
            }
        };

        blockTable = new JTable(model);
        blockTable.getColumn("ID").setMinWidth(0);
        blockTable.getColumn("ID").setMaxWidth(0);
        blockTable.getColumn("ID").setWidth(0); // Hide ID column

        blockTable.setRowHeight(27);
        blockTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        blockTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(blockTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Block List"));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        add(mainPanel);

        // Load initial data
        loadBlockList(user.getId());

        // Back button event
        backButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }

    private void loadBlockList(int userId) throws Exception {
        List<User> blockLists = blockController.getBlockList(userId);
        model.setRowCount(0); // Clear existing rows
        for (User block : blockLists) {
            model.addRow(new Object[]{block.getId(), block.getUsername(), "Actions"});
        }
    }

    // Custom cell renderer for buttons
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton cancelButton = new JButton("Cancel Block");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(cancelButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor for buttons
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton cancelButton = new JButton("Cancel Block");
        private final int currentUserId;

        public ButtonEditor(JCheckBox checkBox, int currentUserId) {
            this.currentUserId = currentUserId;

            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(cancelButton);

            // Accept button event
            cancelButton.addActionListener(e -> {
                int row = blockTable.getSelectedRow();
                int blockId = (int) blockTable.getValueAt(row, 0);

                if (row >= 0){
                    try {
                        if (blockController.cancelBlock(currentUserId, blockId)) {
                            JOptionPane.showMessageDialog(panel, "Cancel block!");
                            stopCellEditing();
                            model.removeRow(row); // Remove row after action
                        } else {
                            JOptionPane.showMessageDialog(panel, "Failed to cancel!");
                        }
                    } catch (HeadlessException e1) {
                        e1.printStackTrace();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    fireEditingStopped();
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

