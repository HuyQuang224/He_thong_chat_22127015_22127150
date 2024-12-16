package ui;

import bus.UserAccountBus;
import dao.LoginHistoryDAO;
import dao.UserAccountDAO;
import dao.UserFriendDAO;
import datastructure.LoginHistory;
import datastructure.UserAccount;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class UserAccountUI extends JPanel {
    private JFrame frame;
    private UserAccountBus userAccountBus;
    private JTable userTable;
    private UserAccountDAO userAccountDAO;
    private DefaultTableModel tableModel;
    private JTable loginHistoryTable;  
    private DefaultTableModel loginHistoryTableModel; 
    private LoginHistoryDAO loginHistoryDAO;
    private JTable friendTable;
    private DefaultTableModel friendTableModel;
    private UserFriendDAO userFriendDAO;

    public UserAccountUI() {
        userAccountDAO = new UserAccountDAO();
        userAccountBus = new UserAccountBus();
        loginHistoryDAO = new LoginHistoryDAO(); 
        userFriendDAO = new UserFriendDAO();

        initialize();
        startAutoRefresh();
    }

    private void initialize() {
        frame = new JFrame("User Management");
        frame.setBounds(100, 100, 800, 600); // Tăng kích thước cửa sổ để bảng rộng hơn
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));
    
        // Tạo bảng để hiển thị danh sách người dùng
        String[] columnNames = {
            "ID", "Username", "Password", "Full Name", "Address", "Date of Birth", "Gender", "Email", "Online", "Created At", "Banned"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Tạo bảng hiển thị lịch sử đăng nhập
        String[] loginHistoryColumnNames = {"Login ID", "User ID", "Username", "Login Time"};
        loginHistoryTableModel = new DefaultTableModel(loginHistoryColumnNames, 0);
        loginHistoryTable = new JTable(loginHistoryTableModel);
        JScrollPane loginHistoryScrollPane = new JScrollPane(loginHistoryTable);
        panel.add(loginHistoryScrollPane, BorderLayout.SOUTH);  

        // Tạo bảng để hiển thị danh sách bạn bè
        String[] friendsColumnNames = {"User ID", "Friend ID"};
        friendTableModel = new DefaultTableModel(friendsColumnNames, 0);
        friendTable = new JTable(friendTableModel);
        JScrollPane friendsScrollPane = new JScrollPane(friendTable);
        panel.add(friendsScrollPane, BorderLayout.EAST);  // Bạn có thể thay đổi vị trí tùy theo mong muốn

        // Các panel điều khiển
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS)); // Sử dụng BoxLayout theo chiều dọc
        panel.add(controlPanel, BorderLayout.NORTH);
    
        // Thêm nút "View Friends" trong panel điều khiển
        JPanel viewFriendsPanel = new JPanel();
        controlPanel.add(viewFriendsPanel);
        JButton viewFriendsButton = new JButton("View Friends");
        viewFriendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lấy ID người dùng được chọn từ bảng người dùng
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int userID = (int) userTable.getValueAt(selectedRow, 0); 
                    viewFriends(userID); 
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a user.");
                }
            }
        });
        viewFriendsPanel.add(viewFriendsButton);

        // Thêm các lựa chọn lọc
        JPanel filterPanel = new JPanel();
        controlPanel.add(filterPanel);
        filterPanel.add(new JLabel("Filter by: "));
        JComboBox<String> filterComboBox = new JComboBox<>(new String[] {"All", "Online", "Banned"});
        filterPanel.add(filterComboBox);
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String filterCriteria = (String) filterComboBox.getSelectedItem();
                    List<UserAccount> filteredUsers = userAccountDAO.filterUsers(filterCriteria);
                    updateUserTable(filteredUsers);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error filtering users.");
                }
            }
        });
        filterPanel.add(filterButton);
    
        // Thêm nút Ban/Unban
        JPanel banPanel = new JPanel();
        controlPanel.add(banPanel);
        JButton banButton = new JButton("Ban/Unban User");
        banButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleBannedStatus();  
            }
        });
        banPanel.add(banButton);

        // Tạo bảng sắp xếp với TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);

    
        // Thêm nút Xóa Người Dùng
        JPanel deletePanel = new JPanel();
        controlPanel.add(deletePanel);
        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        deletePanel.add(deleteButton);
    
        // Thêm nút Thêm Người Dùng
        JPanel addPanel = new JPanel();
        controlPanel.add(addPanel);
        JButton addButton = new JButton("Add User");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        addPanel.add(addButton);
    
        // Thêm nút Cập Nhật Người Dùng
        JPanel updatePanel = new JPanel();
        controlPanel.add(updatePanel);
        JButton updateButton = new JButton("Update User");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUser(); 
            }
        });
        updatePanel.add(updateButton);
    
        // Thêm nút Cập Nhật Mật Khẩu
        JPanel updatePasswordPanel = new JPanel();
        controlPanel.add(updatePasswordPanel);
        JButton updatePasswordButton = new JButton("Update Password");
        updatePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword(); 
            }
        });
        updatePasswordPanel.add(updatePasswordButton);

        // Nút khởi tạo mật khẩu
        JButton resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JOptionPane.showInputDialog(frame, "Enter the user's email to reset password:");
                if (email != null && !email.isEmpty()) {
                    try {
                        userAccountBus.resetPassword(email); 
                        JOptionPane.showMessageDialog(frame, "Password has been reset and sent to the email.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error sending email: " + ex.getMessage());
                    }
                }
            }
        });
        controlPanel.add(resetPasswordButton);
        
        viewAllLoginHistory();
        loadUsers(); 
    }
    

    private void loadUsers() {
        try {
            List<UserAccount> users = userAccountDAO.getAllUsers();
            updateUserTable(users);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading users.");
        }
    }


    public void addUser() {
        // Nhập thông tin người dùng qua hộp thoại
        String username = JOptionPane.showInputDialog(frame, "Enter username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username cannot be empty.");
            return;
        }

        String password = JOptionPane.showInputDialog(frame, "Enter password:");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Password cannot be empty.");
            return;
        }

        String fullName = JOptionPane.showInputDialog(frame, "Enter full name:");
        if (fullName == null || fullName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Full Name cannot be empty.");
            return;
        }

        String address = JOptionPane.showInputDialog(frame, "Enter address:");
        if (address == null || address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Address cannot be empty.");
            return;
        }

        String email = JOptionPane.showInputDialog(frame, "Enter email:");
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Email cannot be empty.");
            return;
        }
        
        // Chọn giới tính (Male, Female, Other)
        String[] genders = {"Nam", "Nữ", " "};
        String gender = (String) JOptionPane.showInputDialog(frame, "Select gender:",
            "Gender Selection", JOptionPane.QUESTION_MESSAGE, null, genders, genders[0]);
        if (gender == null) {
            gender = "Nam"; 
        }

        // Nhập ngày sinh
        String dateOfBirthString = JOptionPane.showInputDialog(frame, "Enter Date of Birth (yyyy-MM-dd):");
        Date dateOfBirth = null;
        if (dateOfBirthString != null && !dateOfBirthString.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateOfBirth = sdf.parse(dateOfBirthString);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use yyyy-MM-dd.");
                return;
            }
        }

        // Tạo đối tượng UserAccount với thông tin đã nhập
        UserAccount user = new UserAccount(0, username, password, fullName, address, dateOfBirth, gender, email, true, new Date(), false);

        // Gọi phương thức thêm người dùng vào hệ thống
        if (userAccountBus.addUser(user)) {
            JOptionPane.showMessageDialog(frame, "User added successfully.");
            loadUsers(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Error adding user.");
        }
    }

    private void updateUserTable(List<UserAccount> users) {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Add new data to the table
        for (UserAccount user : users) {
            tableModel.addRow(new Object[] {
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFullName(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getEmail(),
                user.isOnline() ? "Online" : "Offline",
                user.getCreatedAt(),
                user.isBanned() ? "Yes" : "No"
            });
        }
    }


// Hàm xóa người dùng
    public void deleteUser() {
        // Lấy thông tin của người dùng đang chọn từ bảng
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to delete.");
            return;
        }

        // Lấy ID người dùng từ hàng đã chọn
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Xác nhận xóa người dùng
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa người dùng khỏi cơ sở dữ liệu
            if (userAccountBus.deleteUser(userId)) {
                JOptionPane.showMessageDialog(frame, "User deleted successfully.");
                loadUsers(); 
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting user.");
            }
        }
    }

    private void startAutoRefresh() {
        Thread autoRefreshThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // Lặp lại sau mỗi 5 giây
                    loadUsers();
                    viewAllLoginHistory();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        autoRefreshThread.setDaemon(true); // Đảm bảo thread này không làm dừng ứng dụng
        autoRefreshThread.start();
    }

    public void updateUser() { 
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to update.");
            return;
        }
    
        // Lấy thông tin hiện tại từ dòng được chọn
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username ;
        String password ;
        String fullName ;
        String address ;
        String email;
    
        // Nhập thông tin qua hộp thoại
        username = JOptionPane.showInputDialog(frame, "Enter username:");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username cannot be empty.");
            return;
        }
    
        password = JOptionPane.showInputDialog(frame, "Enter password:");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Password cannot be empty.");
            return;
        }
    
        fullName = JOptionPane.showInputDialog(frame, "Enter full name:");
        if (fullName == null || fullName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Full Name cannot be empty.");
            return;
        }
    
        address = JOptionPane.showInputDialog(frame, "Enter address:");
        if (address == null || address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Address cannot be empty.");
            return;
        }
    
        email = JOptionPane.showInputDialog(frame, "Enter email:");
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Email cannot be empty.");
            return;
        }
    
        // Chọn giới tính
        String[] genders = {"Nam", "Nữ", " "};
        String gender = (String) JOptionPane.showInputDialog(frame, "Select gender:",
            "Gender Selection", JOptionPane.QUESTION_MESSAGE, null, genders, genders[0]);
        if (gender == null) {
            gender = "Nam"; 
        }
    
        // Nhập ngày sinh
        String dateOfBirthString = JOptionPane.showInputDialog(frame, "Enter Date of Birth (yyyy-MM-dd):");
        Date dateOfBirth = null;
        if (dateOfBirthString != null && !dateOfBirthString.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateOfBirth = sdf.parse(dateOfBirthString);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Please use yyyy-MM-dd.");
                return;
            }
        }
    
        // Trạng thái online
        boolean isOnline = JOptionPane.showConfirmDialog(frame, "Is user online?", 
            "Online Status", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    
        // Trạng thái bị cấm
        boolean isBanned = JOptionPane.showConfirmDialog(frame, "Is user banned?", 
            "Banned Status", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    
        // Cập nhật thông tin
        UserAccount updatedUser = new UserAccount(id, username, password, fullName, address, dateOfBirth, gender, email, isOnline, new Date(), isBanned);
    
        if (userAccountBus.updateUser(updatedUser)) {
            JOptionPane.showMessageDialog(frame, "User updated successfully.");
            loadUsers(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Error updating user.");
        }
    }
    
    public void toggleBannedStatus() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to change banned status.");
            return;
        }
         // Lấy thông tin người dùng từ dòng được chọn
        int userId = (int) tableModel.getValueAt(selectedRow, 0); 

        // Lấy thông tin người dùng từ dòng được chọn
        String isBannedStr = (String) tableModel.getValueAt(selectedRow, 10);
        
        // Chuyển đổi trạng thái isBanned từ chuỗi thành boolean
        boolean isBanned = isBannedStr.equalsIgnoreCase("Yes");
    
        // Hiển thị hộp thoại xác nhận thay đổi trạng thái
        int option = JOptionPane.showConfirmDialog(frame, 
            isBanned ? "Are you sure you want to unban this user?" : "Are you sure you want to ban this user?",
            "Change User Status", JOptionPane.YES_NO_OPTION);
    
        if (option == JOptionPane.YES_OPTION) {
            // Đảo ngược trạng thái isBanned
            isBanned = !isBanned;
    
        
    
            // Gọi phương thức cập nhật trạng thái bị cấm vào hệ thống
            if (userAccountBus.updateUserBannedStatus(userId,isBanned)) {
                JOptionPane.showMessageDialog(frame, "User banned status updated successfully.");
                loadUsers(); 
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating user banned status.");
            }
        }
    }
    
    public void changePassword() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a user to change password.");
            return;
        }
    
        // Lấy thông tin người dùng từ dòng được chọn
        int userId = (int) tableModel.getValueAt(selectedRow, 0); 
    
        // Hiển thị hộp thoại nhập mật khẩu mới
        String newPassword = JOptionPane.showInputDialog(frame, "Enter new password:");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Password cannot be empty.");
            return;
        }
    
        // Gọi phương thức cập nhật mật khẩu
        if (userAccountBus.updatePassword(userId, newPassword)) {
            JOptionPane.showMessageDialog(frame, "Password updated successfully.");
            loadUsers(); 
        } else {
            JOptionPane.showMessageDialog(frame, "Error updating password.");
        }
    }

    // Phương thức tải tất cả lịch sử đăng nhập từ DAO và hiển thị trên bảng
    private void viewAllLoginHistory() {
        try {
            
            List<LoginHistory> loginHistoryList = loginHistoryDAO.getAllLoginHistory();

            
            updateLoginHistoryTable(loginHistoryList);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving login history.");
        }
    }

    // Cập nhật bảng lịch sử đăng nhập
    private void updateLoginHistoryTable(List<LoginHistory> loginHistoryList) {
        // Xóa tất cả dữ liệu cũ trong bảng
        loginHistoryTableModel.setRowCount(0);

        // Thêm các dòng dữ liệu mới
        for (LoginHistory loginHistory : loginHistoryList) {
            Object[] rowData = {
                    loginHistory.getID(),
                    loginHistory.getUserID(),
                    loginHistory.getUserName(),
                    loginHistory.getLoginTime()  
            };
            loginHistoryTableModel.addRow(rowData);
        }
    }

    // Phương thức hiển thị danh sách bạn bè
    private void viewFriends(int userID) {
        try {
            // Lấy danh sách bạn bè từ DAO
            List<UserAccount> friendList = userFriendDAO.getFriendListByUserID(userID);

            // Cập nhật bảng danh sách bạn bè
            updateFriendTable(friendList);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error retrieving friends.");
        }
    }

    // Cập nhật bảng danh sách bạn bè
    private void updateFriendTable(List<UserAccount> friendList) {
        DefaultTableModel model = (DefaultTableModel) friendTable.getModel();
        model.setRowCount(0);  // Xóa tất cả các hàng trong bảng trước khi thêm mới
    
        for (UserAccount friend : friendList) {
            model.addRow(new Object[] { friend.getId(), friend.getUsername() });
        }
    }


    public void showUI() {
        frame.setVisible(true);
    }
}
