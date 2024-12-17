package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;

public class friendDAO {

    public List<User> searchFriends(String name, String mainUsername, int mainUserId) {
        List<User> users = new ArrayList<>();
        String query = """
            SELECT * 
            FROM USER_ACCOUNT u
            WHERE u.fullname LIKE ? 
              AND u.username != ? 
              AND u.id NOT IN (
                  SELECT FRIEND_ID 
                  FROM USER_FRIEND 
                  WHERE ID = ?
                  UNION
                  SELECT ID
                  FROM USER_FRIEND
                  WHERE FRIEND_ID = ?
              )
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
            statement.setString(1, "%" + name + "%"); // Tìm kiếm theo fullname với từ khóa
            statement.setString(2, mainUsername);         // Loại bỏ người đang tìm
            statement.setInt(3, mainUserId);              // ID người đang tìm (bạn bè của họ)
            statement.setInt(4, mainUserId);              // ID người đang tìm (ngược lại)
            statement.setInt(5, mainUserId);              // ID người đang tìm (người họ chặn)
            statement.setInt(6, mainUserId);              // ID người đang tìm (người chặn họ)
    
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


    public boolean sendFriendRequest(int fromId, int toId) throws Exception {
        String checkQuery = "SELECT * FROM FRIEND_REQUEST WHERE FROM_ID = ? AND TO_ID = ? OR FROM_ID = ? AND TO_ID = ?";
        String insertQuery = "INSERT INTO FRIEND_REQUEST (FROM_ID, TO_ID, STATUS, TRY) VALUES (?, ?, 'Pending', 1)";
        try (Connection conn = Util.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            checkStmt.setInt(1, fromId);
            checkStmt.setInt(2, toId);
            checkStmt.setInt(3, toId);
            checkStmt.setInt(4, fromId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // Yêu cầu đã tồn tại
            }
            insertStmt.setInt(1, fromId);
            insertStmt.setInt(2, toId);
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getFriendRequests(int userId) throws Exception {
        List<User> requests = new ArrayList<>();
        String query = "SELECT fr.FROM_ID, ua.username, ua.fullname " +
                       "FROM FRIEND_REQUEST fr " +
                       "JOIN USER_ACCOUNT ua ON fr.FROM_ID = ua.id " +
                       "WHERE fr.TO_ID = ? AND fr.STATUS = 'Pending'";

        try (Connection conn = Util.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int fromId = resultSet.getInt("FROM_ID");
                String username = resultSet.getString("username");
                String fullname = resultSet.getString("fullname");
                requests.add(new User(username, fullname, fromId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<User> getFilteredFriendRequests(int userId, String filterName) throws Exception {
        List<User> requests = new ArrayList<>();
        String query = "SELECT fr.FROM_ID, ua.username, ua.fullname " +
                       "FROM FRIEND_REQUEST fr " +
                       "JOIN USER_ACCOUNT ua ON fr.FROM_ID = ua.id " +
                       "WHERE fr.TO_ID = ? AND fr.STATUS = 'Pending' AND ua.fullname LIKE ?";

        try (Connection conn = Util.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, "%" + filterName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int fromId = resultSet.getInt("FROM_ID");
                String username = resultSet.getString("username");
                String fullname = resultSet.getString("fullname");
                requests.add(new User(username, fullname, fromId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public boolean acceptFriendRequest(int userId, int requesterId) throws Exception {
        String deleteRequestQuery = "DELETE FROM FRIEND_REQUEST WHERE FROM_ID = ? AND TO_ID = ?";
        String addFriendQuery = "INSERT INTO USER_FRIEND (ID, FRIEND_ID) VALUES (?, ?), (?, ?)";

        try (Connection conn = Util.getConnection();
             PreparedStatement deleteStatement = conn.prepareStatement(deleteRequestQuery);
             PreparedStatement addFriendStatement = conn.prepareStatement(addFriendQuery)) {

            conn.setAutoCommit(false);

            // Xóa lời mời
            deleteStatement.setInt(1, requesterId);
            deleteStatement.setInt(2, userId);
            deleteStatement.executeUpdate();

            // Thêm bạn bè (song phương)
            addFriendStatement.setInt(1, userId);
            addFriendStatement.setInt(2, requesterId);
            addFriendStatement.setInt(3, requesterId);
            addFriendStatement.setInt(4, userId);
            addFriendStatement.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean declineFriendRequest(int userId, int requesterId) throws Exception {
        String query = "DELETE FROM FRIEND_REQUEST WHERE FROM_ID = ? AND TO_ID = ?";

        try (Connection conn = Util.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, requesterId);
            preparedStatement.setInt(2, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFriend(int userId, int friendId) throws Exception {
        String query = "DELETE FROM USER_FRIEND WHERE (ID = ? AND FRIEND_ID = ?) OR (ID = ? AND FRIEND_ID = ?)";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean blockUser(int userId, int blockId) throws Exception {
        String removeFriendQuery = "DELETE FROM USER_FRIEND WHERE (ID = ? AND FRIEND_ID = ?) OR (ID = ? AND FRIEND_ID = ?)";
        String blockQuery = "INSERT INTO USER_BLOCK (ID, BLOCK_ID) VALUES (?, ?)";
        Connection conn = Util.getConnection();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement removeStmt = conn.prepareStatement(removeFriendQuery);
                 PreparedStatement blockStmt = conn.prepareStatement(blockQuery)) {

                // Remove from friend list
                removeStmt.setInt(1, userId);
                removeStmt.setInt(2, blockId);
                removeStmt.setInt(3, blockId);
                removeStmt.setInt(4, userId);
                removeStmt.executeUpdate();

                // Add to block list
                blockStmt.setInt(1, userId);
                blockStmt.setInt(2, blockId);
                blockStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<User> getFriendsByUserId(int userId) throws Exception {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.online, u.fullname " +
                     "FROM USER_ACCOUNT u " +
                     "JOIN USER_FRIEND uf ON u.id = uf.FRIEND_ID " +
                     "WHERE uf.id = ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String fullname = rs.getString("fullname");
                    int online = rs.getInt("online");
                    if (online == 1){
                        friends.add(new User(id, username, fullname, "Online"));
                    }
                    else {
                        friends.add(new User(id, username, fullname, "Offline"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return friends;
    }

    public List<User> getFriendsByFilter(int userId, String filterName) throws Exception {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.online, u.fullname " +
                     "FROM USER_ACCOUNT u " +
                     "JOIN USER_FRIEND uf ON u.id = uf.FRIEND_ID " +
                     "WHERE uf.ID = ? AND u.fullname LIKE ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + filterName + "%");
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String fullname = rs.getString("fullname");
                    int online = rs.getInt("online");
                    if (online == 1){
                        friends.add(new User(id, username, fullname, "Online"));
                    }
                    else {
                        friends.add(new User(id, username, fullname, "Offline"));
                    }
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return friends;
    }

    public List<User> updateFriendStatus(List<User> users) throws Exception {
        String sql = "SELECT id, online FROM USER_ACCOUNT WHERE id = ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (User user : users) {
                stmt.setInt(1, user.getId()); // Gán ID của người dùng vào câu lệnh
        
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int online = rs.getInt("online"); // Lấy status từ CSDL
                        if (online == 1){
                            user.setStatus("Online"); // Cập nhật status vào đối tượng User
                        }
                        else {
                            user.setStatus("Offline"); // Cập nhật status vào đối tượng User
                        }
                    }
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> getBlockList(int userId) throws Exception {
        List<User> blockList = new ArrayList<>();
        String query = "SELECT u.id, u.username FROM USER_ACCOUNT u JOIN USER_BLOCK b ON u.id = b.BLOCK_ID WHERE b.ID = ?;";

        try (Connection conn = Util.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int fromId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                blockList.add(new User(fromId, username));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blockList;
    }

    public boolean cancelBlock(int userId, int blockId) throws Exception {
        String query = "DELETE FROM USER_BLOCK WHERE ID = ? AND BLOCK_ID = ?";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, blockId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getOnlineFriendsFilter(int userId, String filterName) throws Exception {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.online, u.fullname " +
                     "FROM USER_ACCOUNT u " +
                     "JOIN USER_FRIEND uf ON u.id = uf.FRIEND_ID " +
                     "WHERE uf.id = ? AND u.fullname LIKE ? AND u.online = '1'";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, "%" + filterName + "%");
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("username");
                    String fullname = rs.getString("fullname");
                    friends.add(new User(username, fullname, id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return friends;
    }


    public List<User> getFriendsInGroup(int userId, int groupChatId) throws Exception {
        List<User> friends = new ArrayList<>();
        
        String query = "SELECT u.ID, u.USERNAME, gm.POSITION " +
                        "FROM USER_ACCOUNT u " +
                        "JOIN GROUPCHAT_MEMBER gm ON u.ID = gm.MEMBER_ID " +
                        "WHERE gm.GROUPCHAT_ID = ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            // Set the parameters for the query
            stmt.setInt(1, groupChatId);
    
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    // Create a new User object and populate it with the data
                    int id = resultSet.getInt("ID");
                    String username = resultSet.getString("USERNAME");
                    String position = resultSet.getString("POSITION"); // Get POSITION
                    friends.add(new User(username, id, position));
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    
        return friends;
    }
}
