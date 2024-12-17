package view;

import controller.friendController;
import controller.logInController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RequestList extends JFrame {
    private friendController requestController;
    private DefaultTableModel model;
    private JTable requestTable;

    public RequestList(User user) throws Exception {
        requestController = new friendController();

        setTitle("Friend Request List");
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

        // Friend Request table
        String[] columnNames = {"ID", "Username", "Fullname", "Actions"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cột Actions có thể chỉnh sửa
            }
        };

        requestTable = new JTable(model);
        requestTable.getColumn("ID").setMinWidth(0);
        requestTable.getColumn("ID").setMaxWidth(0);
        requestTable.getColumn("ID").setWidth(0); // Hide ID column

        requestTable.setRowHeight(27);
        requestTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        requestTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), user.getId()));

        JScrollPane tableScrollPane = new JScrollPane(requestTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Friend Requests"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(tableScrollPane, gbc);

        add(mainPanel);

        // Load initial data
        loadFriendRequests(user.getId());

        filterButton.addActionListener(e -> {
            String filterName = filterField.getText().trim();
            friendController controller = new friendController();
            List<User> filteredRequests;
            try {
                filteredRequests = controller.getFilteredFriendRequest(user.getId(), filterName);

                DefaultTableModel model1 = (DefaultTableModel) requestTable.getModel();
                model1.setRowCount(0); // Clear existing rows

                for (User request : filteredRequests) {
                    model1.addRow(new Object[]{
                        request.getId(),
                        request.getUsername(),
                        request.getFullName(),
                        ""
                    });
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

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

    private void loadFriendRequests(int userId) throws Exception {
        List<User> requests = requestController.getFriendRequests(userId);
        model.setRowCount(0); // Clear existing rows
        for (User request : requests) {
            model.addRow(new Object[]{request.getId(), request.getUsername(), request.getFullName(), "Actions"});
        }
    }

    // Custom cell renderer for buttons
    static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton acceptButton = new JButton("Accept");
        private final JButton declineButton = new JButton("Decline");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(acceptButton);
            add(declineButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Custom cell editor for buttons
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton acceptButton = new JButton("Accept");
        private final JButton declineButton = new JButton("Decline");
        private final int currentUserId;

        public ButtonEditor(JCheckBox checkBox, int currentUserId) {
            this.currentUserId = currentUserId;

            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.add(acceptButton);
            panel.add(declineButton);

            // Accept button event
            acceptButton.addActionListener(e -> {
                int row = requestTable.getSelectedRow();
                int requesterId = (int) requestTable.getValueAt(row, 0);

                if (row >= 0){
                    
                }
                try {
                    if (requestController.acceptFriendRequest(currentUserId, requesterId)) {
                        JOptionPane.showMessageDialog(panel, "Friend request accepted!");
                        stopCellEditing();
                        model.removeRow(row); // Remove row after action
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to accept the request!");
                    }
                } catch (HeadlessException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                fireEditingStopped();
            });

            // Decline button event
            declineButton.addActionListener(e -> {
                int row = requestTable.getSelectedRow();
                int requesterId = (int) requestTable.getValueAt(row, 0);

                try {
                    if (requestController.declineFriendRequest(currentUserId, requesterId)) {
                        JOptionPane.showMessageDialog(panel, "Friend request declined!");
                        stopCellEditing();
                        model.removeRow(row); // Remove row after action
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to decline the request!");
                    }
                } catch (HeadlessException e1) {
                    e1.printStackTrace();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                fireEditingStopped();
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
