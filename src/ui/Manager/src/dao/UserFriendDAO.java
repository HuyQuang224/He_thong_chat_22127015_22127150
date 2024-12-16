package dao;

import datastructure.UserAccount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DatabaseConnection;

public class UserFriendDAO {

    private Connection connection;

    public UserFriendDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Phương thức lấy danh sách bạn bè của người dùng theo userID
    public List<UserAccount> getFriendListByUserID(int userID) throws SQLException {
        List<UserAccount> friendList = new ArrayList<>();
        
        // Truy vấn cơ sở dữ liệu lấy danh sách bạn bè của người dùng
        String query = "SELECT u.ID, u.username, u.password, u.fullname, u.address, u.date_of_birth, u.gender, u.email, u.online, u.created_at, u.banned "
                     + "FROM USER_ACCOUNT u "
                     + "INNER JOIN USER_FRIEND uf ON u.ID = uf.FRIEND_ID "
                     + "WHERE uf.ID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tạo đối tượng UserAccount từ kết quả truy vấn và thêm vào danh sách bạn bè
                    UserAccount friend = new UserAccount(
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
                    friendList.add(friend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return friendList;
    }

    // Phương thức lấy danh sách tất cả người dùng và số lượng bạn bè của họ
    public List<UserAccount> getUserFriends() throws SQLException {
        List<UserAccount> userAccounts = new ArrayList<>();
        
        // Truy vấn cơ sở dữ liệu để lấy tất cả người dùng và số lượng bạn bè của họ
        String query = "SELECT U.ID, U.USERNAME, COUNT(F.FRIEND_ID) AS FRIEND_COUNT "
                     + "FROM USER_ACCOUNT U "
                     + "LEFT JOIN USER_FRIEND F ON U.ID = F.ID "
                     + "GROUP BY U.ID, U.USERNAME";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                int friendCount = rs.getInt("FRIEND_COUNT");

                // Tạo đối tượng UserAccount và không cần set friendCount vì đã có thông tin trong truy vấn
                System.out.println("User: " + username + " has " + friendCount + " friends");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return userAccounts;
    }
    
    // Phương thức đếm số lượng bạn bè của người dùng theo userID
    public int getFriendCountByUserID(int userID) throws SQLException {
        int friendCount = 0;
        
        // Truy vấn cơ sở dữ liệu để đếm số lượng bạn bè của người dùng
        String query = "SELECT COUNT(F.FRIEND_ID) AS FRIEND_COUNT "
                     + "FROM USER_FRIEND F "
                     + "WHERE F.ID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    friendCount = rs.getInt("FRIEND_COUNT");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return friendCount;
    }


    // Phương thức lấy danh sách người dùng
    public List<UserAccount> getUserList() throws SQLException {
        List<UserAccount> userAccounts = new ArrayList<>();
        
        String query = "SELECT ID, USERNAME, PASSWORD, FULLNAME, ADDRESS, DATE_OF_BIRTH, GENDER, EMAIL, ONLINE, CREATED_AT, BANNED "
                     + "FROM USER_ACCOUNT";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String fullName = rs.getString("FULLNAME");
                String address = rs.getString("ADDRESS");
                Date dateOfBirth = rs.getDate("DATE_OF_BIRTH");
                String gender = rs.getString("GENDER");
                String email = rs.getString("EMAIL");
                boolean online = rs.getBoolean("ONLINE");
                Date createdAt = rs.getDate("CREATED_AT");
                boolean banned = rs.getBoolean("BANNED");
                
                UserAccount user = new UserAccount(userId, username, password, fullName, address, dateOfBirth, gender, email, online, createdAt, banned);
                userAccounts.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        
        return userAccounts;
    }

}

