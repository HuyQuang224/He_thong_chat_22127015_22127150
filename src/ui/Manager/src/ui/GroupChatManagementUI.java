package ui;

import bus.GroupChatBUS;
import datastructure.GroupChat;
import datastructure.UserAccount;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class GroupChatManagementUI extends JFrame {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private GroupChatBUS groupChatBUS;
    private TableRowSorter<TableModel> sorter;

    public GroupChatManagementUI() {
        this.groupChatBUS = new GroupChatBUS();
        initUI();
        loadData();
        startAutoRefresh(); 
    }

    private void initUI() {
        // Tạo JFrame chính
        frame = new JFrame("Group Chat Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.setLayout(new BorderLayout());
    
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    
        // Tạo JTextField để nhập tên nhóm chat
        searchField = new JTextField(20); // Chiều dài khung nhập liệu
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchGroupByName(searchField.getText());
            }
        });
    
        searchPanel.add(new JLabel("Search Group:"));
        searchPanel.add(searchField);
    
        frame.add(searchPanel, BorderLayout.NORTH);
    
        // Bảng hiển thị dữ liệu
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"ID", "Group_Name", "Created_At", "Online"});
        table = new JTable(tableModel);
    
        // Sử dụng TableRowSorter để hỗ trợ sắp xếp
        sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
    
        // Tạo JScrollPane cho bảng
        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.CENTER);
    
        // Panel chứa các nút điều khiển
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    
        // Nút "View Members"
        JButton viewMembersButton = new JButton("View Members");
        viewMembersButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int groupChatId = (int) table.getValueAt(selectedRow, 0); // Giả sử cột 0 là ID
                showGroupMembersOnly(groupChatId);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a group to view its members.");
            }
        });
        buttonPanel.add(viewMembersButton);

        // Nút "View Admins"
        JButton viewAdminsButton = new JButton("View Admins");
        viewAdminsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int groupChatId = (int) table.getValueAt(selectedRow, 0); // Giả sử cột 0 là ID
                showGroupAdminsOnly(groupChatId);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a group to view its admins.");
            }
        });
        buttonPanel.add(viewAdminsButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    
        // Hiển thị JFrame
        frame.setLocationRelativeTo(null); // Căn giữa màn hình
        frame.setVisible(true);
    }
    

    private void loadData() {
        ArrayList<GroupChat> groupChats = groupChatBUS.fetchAllGroupChats();
        tableModel.setRowCount(0); // Clear existing data

        for (GroupChat gc : groupChats) {
            tableModel.addRow(new Object[]{
                    gc.getID(),
                    gc.getGroupname(),
                    gc.getCreatedAt(),
                    gc.isOnline() ? "Online" : "Offline"
            });
        }
    }

    private void startAutoRefresh() {
        Thread autoRefreshThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Lặp lại sau mỗi 5 giây
                    loadData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        autoRefreshThread.setDaemon(true); // Đảm bảo thread không giữ ứng dụng
        autoRefreshThread.start();
    }

    // Hàm lọc nhóm chat theo tên (tìm kiếm theo từ khóa)
    private void searchGroupByName(String searchQuery) {
        if (searchQuery.isEmpty()) {
            sorter.setRowFilter(null); // Không lọc nếu ô tìm kiếm trống
        } else {
            // Lọc theo tên nhóm chat (cột 1 trong bảng)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchQuery, 1)); // Tìm kiếm không phân biệt chữ hoa chữ thường
        }
    }

    public void showUI() {
        frame.setVisible(true);
    }

    private void showGroupMembersOnly(int groupChatId) {
        GroupChat groupChat = groupChatBUS.getGroupChatById(groupChatId);
    
        ArrayList<UserAccount> members = groupChat.getMembers();
        String[] columns = {"Member ID", "Username"};
        DefaultTableModel memberTableModel = new DefaultTableModel(columns, 0);
    
        for (UserAccount member : members) {
            memberTableModel.addRow(new Object[]{member.getId(), member.getUsername()});
        }
    
        // Hiển thị bảng thành viên trong cửa sổ mới
        JTable memberTable = new JTable(memberTableModel);
        JScrollPane memberScrollPane = new JScrollPane(memberTable);
        JFrame memberFrame = new JFrame("Group Members");
        memberFrame.setSize(400, 300);
        memberFrame.add(memberScrollPane, BorderLayout.CENTER);
        memberFrame.setLocationRelativeTo(this);
        memberFrame.setVisible(true);
    }
    
    private void showGroupAdminsOnly(int groupChatId) {
        GroupChat groupChat = groupChatBUS.getGroupChatById(groupChatId);
    
        ArrayList<UserAccount> admins = groupChat.getAdmins();
        String[] columns = {"Admin ID", "Username"};
        DefaultTableModel adminTableModel = new DefaultTableModel(columns, 0);
    
        for (UserAccount admin : admins) {
            adminTableModel.addRow(new Object[]{admin.getId(), admin.getUsername()});
        }
    
        // Hiển thị bảng quản trị viên trong cửa sổ mới
        JTable adminTable = new JTable(adminTableModel);
        JScrollPane adminScrollPane = new JScrollPane(adminTable);
        JFrame adminFrame = new JFrame("Group Admins");
        adminFrame.setSize(400, 300);
        adminFrame.add(adminScrollPane, BorderLayout.CENTER);
        adminFrame.setLocationRelativeTo(this);
        adminFrame.setVisible(true);
    }

}
