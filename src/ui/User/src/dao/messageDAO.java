package dao;

import model.Message;
import model.groupChat;
import model.groupMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class messageDAO {
    public void saveMessageToDatabase(int fromUser, int toUser, String content) throws Exception {
        // JDBC logic để lưu tin nhắn vào bảng MESSAGE_USER
        String sql = "INSERT INTO MESSAGE_USER (FROM_USER, TO_USER, CONTENT) " +
                    "VALUES (?, ?, ?)";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromUser);
            stmt.setInt(2, toUser);
            stmt.setString(3, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> loadMessages(int userId, int toUserId) throws Exception {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT ID, FROM_USER, TO_USER, CONTENT, VISIBLE_ONLY " +
                       "FROM MESSAGE_USER " +
                       "WHERE ((FROM_USER = ? AND TO_USER = ?) OR (FROM_USER = ? AND TO_USER = ?)) " +
                       "AND (VISIBLE_ONLY IS NULL OR VISIBLE_ONLY != ?) " +
                       "ORDER BY id ASC";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, userId);
            stmt.setInt(2, toUserId);
            stmt.setInt(3, toUserId);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                int fromUser = rs.getInt("FROM_USER");
                int toUser = rs.getInt("TO_USER");
                String content = rs.getString("CONTENT");
                int visibleOnly = rs.getInt("VISIBLE_ONLY");
                messages.add(new Message(id, fromUser, toUser, content, visibleOnly));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return messages;
    }

    public List<Message> loadfromUserMessages(int fromUserId, int toUserId) throws Exception {
        String query = """
                SELECT id, from_user, to_user, content, visible_only
                FROM MESSAGE_USER
                WHERE (from_user = ? AND to_user = ?)
                AND (VISIBLE_ONLY IS NULL OR VISIBLE_ONLY != ?)
                ORDER BY id ASC;
                """;

        List<Message> messages = new ArrayList<>();

        try (Connection conn = Util.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, toUserId);
            preparedStatement.setInt(2, fromUserId);
            preparedStatement.setInt(3, fromUserId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int fromUser = resultSet.getInt("from_user");
                    int toUser = resultSet.getInt("to_user");
                    String content = resultSet.getString("content");
                    int visibleOnly = resultSet.getInt("visible_only");
                    messages.add(new Message(id, fromUser, toUser, content, visibleOnly));
                }
            }
        }

        return messages;
    }

    public void reportSpam(int reporterId, int reportedUserId, long messageId, String reason) throws Exception {
        String query = "INSERT INTO SPAM_REPORT (REPORTER_ID, REPORTED_USER_ID, MESSAGE_ID, REPORT_REASON, CREATED_AT, STATUS) " +
                       "VALUES (?, ?, ?, ?, NOW(), 'Pending')";
        try (Connection conn = Util.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, reporterId);
            statement.setInt(2, reportedUserId);
            statement.setLong(3, messageId);
            statement.setString(4, reason);
            statement.executeUpdate();
        }
    }

    public void deleteAllMessages(int userId, int toUserId) throws Exception {
        String query = "UPDATE MESSAGE_USER SET VISIBLE_ONLY = ? WHERE (FROM_USER = ? AND TO_USER = ?) OR (FROM_USER = ? AND TO_USER = ?)";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, toUserId);
            stmt.setInt(4, toUserId);
            stmt.setInt(5, userId);
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessageForUser(int messageId, int userId) throws Exception {
        String query = "UPDATE MESSAGE_USER SET VISIBLE_ONLY = ? WHERE ID = ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, userId);
            stmt.setInt(2, messageId);
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessageForEveryone(int messageId) throws Exception {
        String query = "DELETE FROM MESSAGE_USER WHERE ID = ?";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, messageId);
    
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> searchMessages(int userId, int toUserId, String searchText) throws Exception {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT ID, FROM_USER, TO_USER, CONTENT, VISIBLE_ONLY " +
                       "FROM MESSAGE_USER " +
                       "WHERE ((FROM_USER = ? AND TO_USER = ?) OR (FROM_USER = ? AND TO_USER = ?)) " +
                       "AND (VISIBLE_ONLY IS NULL OR VISIBLE_ONLY != ?) " +
                       "AND SUBSTRING_INDEX(CONTENT, ':', -1) LIKE ? " +
                       "ORDER BY id ASC";
    
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, userId);
            stmt.setInt(2, toUserId);
            stmt.setInt(3, toUserId);
            stmt.setInt(4, userId);
            stmt.setInt(5, userId);
            stmt.setString(6, "%" + searchText + "%"); // Tìm kiếm chuỗi với ký tự đại diện
    
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                int fromUser = rs.getInt("FROM_USER");
                int toUser = rs.getInt("TO_USER");
                String content = rs.getString("CONTENT");
                int visibleOnly = rs.getInt("VISIBLE_ONLY");
                messages.add(new Message(id, fromUser, toUser, content, visibleOnly));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return messages;
    }

    public groupChat createGroupChat(String groupName) throws SQLException, Exception {
        String query = "INSERT INTO GROUPCHAT (GROUP_NAME, CREATED_AT) VALUES (?, NOW())";
        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { // Request generated keys
            stmt.setString(1, groupName);
            
            // Execute the update
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating group chat failed, no rows affected.");
            }
            
            // Retrieve the generated keys
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int groupId = rs.getInt(1); // Retrieve the first generated key (ID)
                    return new groupChat(groupId, groupName);
                } else {
                    throw new SQLException("Creating group chat failed, no ID obtained.");
                }
            }
        }
    }


    public void insertMember(int groupId, int memberId, String pos) throws SQLException, Exception {
        String query = "INSERT INTO GROUPCHAT_MEMBER (GROUPCHAT_ID, MEMBER_ID, POSITION) VALUES (?, ?, ?)";
        try (Connection conn = Util.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the parameters
            stmt.setInt(1, groupId);
            stmt.setInt(2, memberId);
            stmt.setString(3, pos);

            // Execute the update
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting member failed, no rows affected.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLException("Member insertion failed: Member may already exist in the group or invalid group ID.", e);
        }
    }

    // Method to get all group chats where the userId is a member
    public List<groupChat> getAllGroupChatsByUserId(int userId) throws Exception {
        List<groupChat> groupChats = new ArrayList<>();

        // Database connection setup (assuming you have a Connection object)
        String query = "SELECT g.ID, g.GROUP_NAME " +
                       "FROM GROUPCHAT g " +
                       "JOIN GROUPCHAT_MEMBER gm ON g.ID = gm.GROUPCHAT_ID " +
                       "WHERE gm.MEMBER_ID = ?";

        try (Connection conn = Util.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the userId parameter for the query
            stmt.setInt(1, userId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    // Create a new GroupChat object and populate it with the data
                    int groupId = resultSet.getInt("ID");
                    String groupName = resultSet.getString("GROUP_NAME");
                    groupChats.add(new groupChat(groupId, groupName));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return groupChats;
    }

    public void renameGroup(int groupId, String newGroupName) {
        String query = "UPDATE GROUPCHAT SET GROUP_NAME = ? WHERE ID = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, newGroupName);
            preparedStatement.setInt(2, groupId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm lưu tin nhắn nhóm vào CSDL
    public void saveGroupMessage(int fromUserId, int toGroupId, String content) throws Exception {
        String sql = "INSERT INTO MESSAGE_GROUP (FROM_USER, TO_GROUP, CONTENT) VALUES (?, ?, ?)";
        
        // Kết nối và thực thi câu lệnh
        try (Connection conn = Util.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Gán giá trị tham số
            pstmt.setInt(1, fromUserId);
            pstmt.setInt(2, toGroupId);
            pstmt.setString(3, content);
            
            // Thực thi câu lệnh INSERT
            int rowsAffected = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hàm truy vấn tin nhắn nhóm từ CSDL theo group ID
    public List<groupMessage> loadGroupMessages(int groupId) throws Exception {
        List<groupMessage> messages = new ArrayList<>();
        String sql = "SELECT ID, FROM_USER, TO_GROUP, CONTENT FROM MESSAGE_GROUP WHERE TO_GROUP = ? ORDER BY ID ASC";

        try (Connection conn = Util.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán tham số
            pstmt.setInt(1, groupId);

            // Thực thi truy vấn
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    int fromUserId = rs.getInt("FROM_USER");
                    int toGroupId = rs.getInt("TO_GROUP");
                    String content = rs.getString("CONTENT");

                    // Thêm tin nhắn vào danh sách
                    messages.add(new groupMessage(id, fromUserId, toGroupId, content));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Hàm tìm kiếm tin nhắn trong một group dựa vào chuỗi tìm kiếm
    public List<groupMessage> searchGroupMessages(int groupId, String searchText) throws Exception {
        List<groupMessage> resultMessages = new ArrayList<>();

        // SQL để tìm kiếm các tin nhắn chứa chuỗi tìm kiếm trong nội dung
        String sql = "SELECT ID, FROM_USER, TO_GROUP, CONTENT FROM MESSAGE_GROUP WHERE TO_GROUP = ? AND CONTENT LIKE ? ORDER BY ID ASC";

        // Chuẩn bị câu lệnh truy vấn
        try (Connection conn = Util.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            // Thêm điều kiện vào câu lệnh truy vấn
            statement.setInt(1, groupId); // ID của group
            statement.setString(2, "%" + searchText + "%"); // Chuỗi tìm kiếm (sử dụng LIKE để tìm kiếm theo mẫu)

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = statement.executeQuery();

            // Duyệt qua các kết quả và thêm vào danh sách
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int fromUserId = resultSet.getInt("FROM_USER");
                int toGroupId = resultSet.getInt("TO_GROUP");
                String content = resultSet.getString("CONTENT");
                resultMessages.add(new groupMessage(id, fromUserId, toGroupId, content));
            }
        }

        return resultMessages;
    }
}
