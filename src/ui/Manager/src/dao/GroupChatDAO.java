package dao;

import datastructure.GroupChat;
import datastructure.UserAccount;
import java.sql.*;
import java.util.ArrayList;
import utils.DatabaseConnection;

public class GroupChatDAO {
    private Connection connection;

    public GroupChatDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public ArrayList<GroupChat> getAllGroupChats() {
        ArrayList<GroupChat> groupChats = new ArrayList<>();
        String query = "SELECT * FROM GROUPCHAT";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                GroupChat groupChat = new GroupChat();
                groupChat.setID(rs.getInt("ID"));
                groupChat.setGroupname(rs.getString("GROUP_NAME"));
                groupChat.setCreatedAt(rs.getString("CREATED_AT"));
                groupChat.setOnline(rs.getBoolean("ONLINE"));
                groupChats.add(groupChat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupChats;
    }

    public ArrayList<GroupChat> fetchSortedGroupChats(String sortBy) {
        ArrayList<GroupChat> groupChats = new ArrayList<>();
        String query = "SELECT * FROM GROUP_CHAT ORDER BY " + sortBy;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GroupChat groupChat = new GroupChat();
                groupChat.setID(rs.getInt("ID"));
                groupChat.setGroupname(rs.getString("GROUP_NAME"));
                groupChat.setCreatedAt(rs.getTimestamp("CREATED_AT").toString());
                groupChat.setOnline(rs.getBoolean("ONLINE"));
                groupChats.add(groupChat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupChats;
    }

    // Truy xuất nhóm chat theo ID
    public GroupChat getGroupChatById(int groupChatId) {
        GroupChat groupChat = null;
        String query = "SELECT gc.ID, gc.GROUP_NAME, gc.ONLINE, gc.CREATED_AT " +
                       "FROM GROUPCHAT gc " +
                       "WHERE gc.ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, groupChatId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                groupChat = new GroupChat();
                groupChat.setID(rs.getInt("ID"));
                groupChat.setGroupname(rs.getString("GROUP_NAME"));
                groupChat.setOnline(rs.getBoolean("ONLINE"));
                groupChat.setCreatedAt(rs.getString("CREATED_AT"));
                
                // Gọi phương thức để lấy thành viên và admin
                loadMembersAndAdmins(groupChat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupChat;
    }

    // Lấy danh sách thành viên và admin của nhóm
    private void loadMembersAndAdmins(GroupChat groupChat) {
        String query = "SELECT m.MEMBER_ID, u.USERNAME, m.POSITION " +
                       "FROM GROUPCHAT_MEMBER m " +
                       "JOIN USER_ACCOUNT u ON m.MEMBER_ID = u.ID " +
                       "WHERE m.GROUPCHAT_ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, groupChat.getID());
            ResultSet rs = ps.executeQuery();
            
            ArrayList<UserAccount> members = new ArrayList<>();
            ArrayList<UserAccount> admins = new ArrayList<>();
            
            while (rs.next()) {
                int memberId = rs.getInt("MEMBER_ID");
                String username = rs.getString("USERNAME");
                String position = rs.getString("POSITION");
                
                UserAccount user = new UserAccount(memberId, username);
                
                // Phân loại thành viên và admin
                if ("admin".equalsIgnoreCase(position)) {
                    admins.add(user);
                } else {
                    members.add(user);
                }
            }
            
            groupChat.setMembers(members);
            groupChat.setAdmins(admins);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
}
