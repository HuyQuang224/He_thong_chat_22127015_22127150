package dao;

import datastructure.LoginHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseConnection;

public class LoginHistoryDAO {

    private Connection connection;

    public LoginHistoryDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<LoginHistory> getAllLoginHistory() throws SQLException {
        List<LoginHistory> loginHistoryList = new ArrayList<>();
        
        // Truy vấn cơ sở dữ liệu lấy tất cả lịch sử đăng nhập với thông tin userName
        String query = "SELECT lh.LOGIN_ID, lh.USER_ID, ua.Username, lh.LOGIN_TIME " +
                       "FROM LOGIN_HISTORY lh " +
                       "JOIN USER_ACCOUNT ua ON lh.USER_ID = ua.ID";
        
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                int id = rs.getInt("LOGIN_ID");
                int userId = rs.getInt("USER_ID");
                String userName = rs.getString("Username");  // Lấy tên người dùng từ bảng USER_ACCOUNT
                String loginTime = rs.getString("LOGIN_TIME");
                
                // Tạo đối tượng LoginHistory và thêm vào danh sách
                LoginHistory loginHistory = new LoginHistory(id, userId, loginTime);
                loginHistory.setUserName(userName);  // Thiết lập userName cho đối tượng LoginHistory
                loginHistoryList.add(loginHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return loginHistoryList;
    }
    
    // Phương thức đếm số lần đăng nhập của người dùng
    public int getLoginCountByUserID(int userID) throws SQLException {
        String sql = "SELECT COUNT(LOGIN_ID) AS loginCount " +
                    "FROM LOGIN_HISTORY " +
                    "WHERE USER_ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("loginCount");
                }
            }
        }
        return 0;  // Nếu không tìm thấy, trả về 0
    }

    // Phương thức lấy danh sách tất cả người dùng
    public List<LoginHistory> getAllUsers() throws SQLException {
        List<LoginHistory> userList = new ArrayList<>();
        
        String sql = "SELECT u.ID, u.Username FROM USER_ACCOUNT u";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int userID = rs.getInt("ID");
                String userName = rs.getString("Username");

                LoginHistory loginHistory = new LoginHistory();
                loginHistory.setUserID(userID);
                loginHistory.setUserName(userName);

                userList.add(loginHistory);
            }
        }
        
        return userList;
    }

    public Timestamp getLatestLoginTimeByUserID(int userID) throws SQLException {
        String query = "SELECT MAX(login_time) FROM login_history WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp(1); // Trả về thời gian đăng nhập gần nhất
                }
            }
        }
        return null; // Trả về null nếu không có dữ liệu
    }

    
}
