package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO {
    public boolean registerUser(User user) throws Exception {
        String sql = "INSERT INTO USER_ACCOUNT (username, fullname, address, date_of_birth, gender, email, password, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getDob());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getEmail());
            stmt.setString(7, user.getPassword());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameExists(String username) throws Exception {
        String sql = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE username = ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0, trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc không tìm thấy
    }

    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE email = ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0, trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc không tìm thấy
    }

    public User getUserByUsernameOrEmailAndPassword(String identifier, String password) {
        try (Connection conn = Util.getConnection()) {
            String query = "SELECT * FROM USER_ACCOUNT WHERE (username = ? OR email = ?) AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, identifier); // Có thể là username hoặc email
            ps.setString(2, identifier);
            ps.setString(3, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("online") == 1){
                    return new User(
                    rs.getInt("id"), 
                    rs.getString("username"),
                    rs.getString("fullname"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getBoolean("banned"),
                    "isUsed");
                }
                return new User(
                    rs.getInt("id"), 
                    rs.getString("username"),
                    rs.getString("fullname"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getBoolean("banned"),
                    "Online");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        try (Connection conn = Util.getConnection()) {
            String query = "UPDATE USER_ACCOUNT SET password = ? WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, newPassword);
            ps.setString(2, email);
    
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateInfo(User user) {
        try (Connection conn = Util.getConnection()) {
            String query = "UPDATE USER_ACCOUNT SET username = ?, fullname = ?, address = ?, date_of_birth = ?, gender = ?, email = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getAddress());
            ps.setDate(4, java.sql.Date.valueOf(user.getDob()));
            ps.setString(5, user.getGender());
            ps.setString(6, user.getEmail());
            ps.setInt(7, user.getId());
    
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUpUsernameExists(String username, int id) throws Exception {
        String sql = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE username = ? AND id != ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         
            stmt.setString(1, username);
            stmt.setInt(2, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0, trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc không tìm thấy
    }

    public boolean isUpEmailExists(String email, int id) throws Exception {
        String sql = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE email = ? AND id != ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
         
            stmt.setString(1, email);
            stmt.setInt(2, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0, trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc không tìm thấy
    }

    public boolean UpdatePassword(User user) {
        try (Connection conn = Util.getConnection()) {
            String query = "UPDATE USER_ACCOUNT SET password = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, user.getPassword());
            ps.setInt(2, user.getId());
    
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<User> searchUsers(String name, String mainUsername, int mainUserId) {
        List<User> users = new ArrayList<>();
        String query = """
            SELECT * 
            FROM USER_ACCOUNT u
            WHERE ((u.username LIKE ?
              OR u.fullname LIKE ?)
              AND u.username != ?)
              AND u.id NOT IN (
                  SELECT BLOCK_ID
                  FROM USER_BLOCK
                  WHERE ID = ?
                  UNION
                  SELECT ID
                  FROM USER_BLOCK
                  WHERE BLOCK_ID = ?
              )
        """;
    
        try (Connection conn = Util.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            // Gán giá trị cho các tham số trong câu truy vấn
            statement.setString(1, "%" + name + "%"); // Tìm kiếm theo username với từ khóa
            statement.setString(2, "%" + name + "%"); // Tìm kiếm theo fullname với từ khóa
            statement.setString(3, mainUsername);         // Loại bỏ người đang tìm
            statement.setInt(4, mainUserId);              // ID người đang tìm (người họ chặn)
            statement.setInt(5, mainUserId);              // ID người đang tìm (người chặn họ)
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String fullname = resultSet.getString("fullname");
                    users.add(new User(username, fullname, userId)); // Thêm user vào danh sách kết quả
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }   
        return users;
    }

    public void updateOnlineStatus(int userId, String status) throws Exception {
        String sql = "UPDATE USER_ACCOUNT SET online = ? WHERE id = ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (status.equals("Online")){
                stmt.setInt(1, 1);
            }
            else {
                stmt.setInt(1, 0);
            }
           
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
