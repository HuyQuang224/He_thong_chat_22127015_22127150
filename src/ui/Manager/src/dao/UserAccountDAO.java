package dao;

import datastructure.UserAccount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseConnection;

public class UserAccountDAO {
    private Connection connection;

    public UserAccountDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<UserAccount> getUsersByYear(int year) throws SQLException {
        List<UserAccount> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE YEAR(created_at) = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserAccount user = new UserAccount();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getDate("created_at"));
                users.add(user);
            }
        }
        return users;
    }
    

    // Lấy tất cả người dùng
    public List<UserAccount> getAllUsers() throws SQLException {
        List<UserAccount> userList = new ArrayList<>();
        String query = "SELECT * FROM USER_ACCOUNT";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                UserAccount user = new UserAccount(
                        rs.getInt("ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("FULLNAME"),
                        rs.getString("ADDRESS"),
                        rs.getDate("DATE_OF_BIRTH"),
                        rs.getString("GENDER"),
                        rs.getString("EMAIL"),
                        rs.getBoolean("ONLINE"),
                        rs.getTimestamp("CREATED_AT"),
                        rs.getBoolean("BANNED")
                );
                userList.add(user);
            }
        }
        return userList;
    }

    // Thêm người dùng mới
    public boolean addUser(UserAccount user) throws SQLException {
        String query = "INSERT INTO USER_ACCOUNT (USERNAME, PASSWORD, FULLNAME, ADDRESS, DATE_OF_BIRTH, GENDER, EMAIL, CREATED_AT, ONLINE, BANNED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getAddress());
            stmt.setDate(5, new java.sql.Date(user.getDateOfBirth().getTime()));
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getEmail());
            stmt.setTimestamp(8, new Timestamp(user.getCreatedAt().getTime()));
            stmt.setBoolean(9, user.isOnline());
            stmt.setBoolean(10, user.isBanned());

            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa người dùng
    public boolean deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM USER_ACCOUNT WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Lọc người dùng theo tên, tên đăng nhập, trạng thái
    public List<UserAccount> filterUsers(String filterCriteria) throws SQLException {
        List<UserAccount> userList = new ArrayList<>();
        String query = "";
    
        if ("Online".equalsIgnoreCase(filterCriteria)) {
            query = "SELECT * FROM USER_ACCOUNT WHERE ONLINE = 1";
        } else if ("Banned".equalsIgnoreCase(filterCriteria)) {
            query = "SELECT * FROM USER_ACCOUNT WHERE BANNED = 1";
        } else {
            query = "SELECT * FROM USER_ACCOUNT"; // Nếu không lọc, lấy tất cả người dùng
        }
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                UserAccount user = new UserAccount(
                        rs.getInt("ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("FULLNAME"),
                        rs.getString("ADDRESS"),
                        rs.getDate("DATE_OF_BIRTH"),
                        rs.getString("GENDER"),
                        rs.getString("EMAIL"),
                        rs.getBoolean("ONLINE"),
                        rs.getTimestamp("CREATED_AT"),
                        rs.getBoolean("BANNED")
                );
                userList.add(user);
            }
        }
        return userList;
    }
    

    // Sắp xếp người dùng theo tên hoặc ngày tạo
    public List<UserAccount> sortUsers(String sortBy) throws SQLException {
        List<UserAccount> userList = new ArrayList<>();
        String query = "";
    
        if ("Name".equalsIgnoreCase(sortBy)) {
            query = "SELECT * FROM USER_ACCOUNT";
        } else if ("CreatedAt".equalsIgnoreCase(sortBy)) {
            query = "SELECT * FROM USER_ACCOUNT ORDER BY CREATED_AT";
        }
    
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                UserAccount user = new UserAccount(
                        rs.getInt("ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("FULLNAME"),
                        rs.getString("ADDRESS"),
                        rs.getDate("DATE_OF_BIRTH"),
                        rs.getString("GENDER"),
                        rs.getString("EMAIL"),
                        rs.getBoolean("ONLINE"),
                        rs.getTimestamp("CREATED_AT"),
                        rs.getBoolean("BANNED")
                );
                userList.add(user);
            }
        }
    
        // Nếu sắp xếp theo tên, sử dụng Comparator để sắp xếp danh sách người dùng
        if ("Name".equalsIgnoreCase(sortBy)) {
            userList.sort((u1, u2) -> {
                // Sắp xếp theo tên đầy đủ
                return u1.getFullName().compareToIgnoreCase(u2.getFullName());
            });
        }
    
        return userList;
    }
    
    // Lấy người dùng theo ID
    public UserAccount getUserById(int userId) throws SQLException {
        UserAccount user = null;
        String query = "SELECT * FROM USER_ACCOUNT WHERE ID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserAccount(
                            rs.getInt("ID"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD"),
                            rs.getString("FULLNAME"),
                            rs.getString("ADDRESS"),
                            rs.getDate("DATE_OF_BIRTH"),
                            rs.getString("GENDER"),
                            rs.getString("EMAIL"),
                            rs.getBoolean("ONLINE"),
                            rs.getTimestamp("CREATED_AT"),
                            rs.getBoolean("BANNED")
                    );
                }
            }
        }
        return user;
    }


    // Cập nhật thông tin người dùng trong cơ sở dữ liệu
    // Cập nhật thông tin người dùng trong cơ sở dữ liệu
    public boolean updateUser(UserAccount user) throws SQLException {
        String query = "UPDATE user_account SET username = ?, password = ?, fullname = ?, address = ?, date_of_birth = ?, "
                     + "gender = ?, email = ?, online = ?, created_at = ?, banned = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getAddress());
            stmt.setDate(5, new java.sql.Date(user.getDateOfBirth().getTime()));
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getEmail());
            stmt.setBoolean(8, user.isOnline());
            stmt.setDate(9, new java.sql.Date(user.getCreatedAt().getTime()));
            stmt.setBoolean(10, user.isBanned());
            stmt.setInt(11, user.getId());
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
        }
    }

    public boolean updateUserBannedStatus(int userId, boolean isBanned) {
        // Giả sử bạn đang sử dụng JDBC để kết nối với cơ sở dữ liệu
        String sql = "UPDATE user_account SET Banned = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setBoolean(1, isBanned);
            statement.setInt(2, userId);
            
            int rowsAffected = statement.executeUpdate();
            
            return rowsAffected > 0; // Trả về true nếu cập nhật thành công, false nếu không có thay đổi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Phương thức cập nhật mật khẩu trong cơ sở dữ liệu
    public boolean updatePassword(int userId, UserAccount user) {
        String sql = "UPDATE user_account SET password = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Nếu có dòng bị ảnh hưởng, tức là cập nhật thành công
        } catch (SQLException e) {
            System.out.println("Error updating password in database: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE user_account SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
