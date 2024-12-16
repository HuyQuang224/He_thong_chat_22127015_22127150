package ui;

import dao.UserFriendDAO;
import datastructure.UserAccount;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class UserFriendUI extends JFrame {
    private JFrame frame;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserFriendDAO userFriendDAO;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JTextField searchField;
    private JComboBox<String> filterCriteria;
    private JPanel filterPanel;

    public UserFriendUI() {
        userFriendDAO = new UserFriendDAO();
        initUI();
        // Gọi phương thức để cập nhật dữ liệu cho bảng
        loadUserFriendsData();
        startAutoRefresh();
    }

    private void initUI() {
        setTitle("User Friends List");
        setSize(900, 600);  // Tăng kích thước để đủ hiển thị thông tin
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
    
        // Tạo bảng để hiển thị thông tin người dùng
        String[] columnNames = {
            "ID", "Username", "Full Name", "Address", "Date of Birth", "Gender", 
            "Email", "Online", "Created At", "Banned", "Friend Count"
        };
    
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        userTable = new JTable(tableModel);
    
        // Tạo JScrollPane cho bảng
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    
        // Thêm TableRowSorter để hỗ trợ sắp xếp
        rowSorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(rowSorter);
    
        // Tạo Panel để chứa ComboBox và trường tìm kiếm
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
    
        // ComboBox để chọn tiêu chí lọc (Username hoặc Friend Count)
        JComboBox<String> filterCriteria = new JComboBox<>(new String[]{"Username", "Friend Count"});
        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(filterCriteria);
    
        // JTextField để nhập từ khóa tìm kiếm
        JTextField searchField = new JTextField(20);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                // Gọi phương thức lọc khi người dùng nhập từ khóa vào trường tìm kiếm
                applyFilter(searchField.getText(), filterCriteria.getSelectedItem().toString());
            }
        });
    
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
    
        // ComboBox - lọc theo tên hoặc số lượng bạn bè
        filterCriteria.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Gọi phương thức lọc lại khi người dùng thay đổi tiêu chí lọc
                applyFilter(searchField.getText(), filterCriteria.getSelectedItem().toString());
            }
        });
    
        add(filterPanel, BorderLayout.NORTH);
        setVisible(true);
    }
    
    
    
    
    
    

    private void loadUserFriendsData() {
        try {
            // Lấy danh sách người dùng từ cơ sở dữ liệu
            List<UserAccount> userList = userFriendDAO.getUserList();
            
            // Xóa dữ liệu cũ trong bảng
            tableModel.setRowCount(0);
    
            // Lặp qua danh sách người dùng và lấy số lượng bạn bè cho mỗi người
            for (UserAccount user : userList) {
                // Gọi phương thức để đếm số lượng bạn bè của người dùng
                int friendCount = userFriendDAO.getFriendCountByUserID(user.getId());
    
                // Tạo mảng dữ liệu cho mỗi dòng
                Object[] row = new Object[] {
                    user.getId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getAddress(),
                    user.getDateOfBirth(),
                    user.getGender(),
                    user.getEmail(),
                    user.isOnline() ? "Online" : "Offline",
                    user.getCreatedAt(),
                    user.isBanned() ? "Banned" : "Active",
                    friendCount
                };
    
                // Thêm dòng vào bảng
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user friends data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void startAutoRefresh() {
        Thread autoRefreshThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Lặp lại sau mỗi 5 giây
                    loadUserFriendsData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        autoRefreshThread.setDaemon(true); // Đảm bảo thread không giữ ứng dụng
        autoRefreshThread.start();
    }

    private void applyFilter(String text, String filterCriteria) {
        RowFilter<DefaultTableModel, Object> rowFilter = null;
    
        try {
            if (filterCriteria.equals("Username")) {
                rowFilter = RowFilter.regexFilter("(?i)" + text, 1);  // Lọc theo cột "Username"
            } else if (filterCriteria.equals("Friend Count")) {
                rowFilter = RowFilter.regexFilter("(?i)" + text, 10);  // Lọc theo cột "FriendCount"
            }
        } catch (java.util.regex.PatternSyntaxException e) {
            return;  // Nếu có lỗi cú pháp, bỏ qua
        }
    
        // Áp dụng RowFilter cho TableRowSorter
        rowSorter.setRowFilter(rowFilter);
    }
    


    public void showUI() {
        setVisible(true);
    }
}
