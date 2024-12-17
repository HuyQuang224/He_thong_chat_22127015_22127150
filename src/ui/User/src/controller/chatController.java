package controller;

import dao.messageDAO;
import model.Message;
import model.groupChat;
import model.groupMessage;

import java.sql.SQLException;
import java.util.List;

public class chatController {
    messageDAO messageDAO = new messageDAO();

    public void saveMessage(int fromId, int toId, String content) throws Exception {
        messageDAO.saveMessageToDatabase(fromId, toId, content);
    }

    public List<Message> loadMessage(int fromId, int toId) throws Exception {
        return messageDAO.loadMessages(fromId, toId);
    }

    public List<Message> loadfromUserMessage(int fromId, int toId) throws Exception {
        return messageDAO.loadfromUserMessages(fromId, toId);
    }

    public void reportSpam(int userId, int reportedUserId, int messageId, String reason) throws Exception {
        messageDAO.reportSpam(userId, reportedUserId, messageId, reason);
    }

    public void deleteAllMessages(int userId, int toUserId) throws Exception {
        messageDAO.deleteAllMessages(userId, toUserId);
    }
    
    public void deleteMessageForUser(int messageId, int userId) throws Exception {
        messageDAO.deleteMessageForUser(messageId, userId);
    }
    
    public void deleteMessageForEveryone(int messageId) throws Exception {
        messageDAO.deleteMessageForEveryone(messageId);
    }

    public List<Message> searchMessages(int fromId, int toId, String searchText) throws Exception {
        return messageDAO.searchMessages(fromId, toId, searchText);
    }

    public groupChat createGroupChat(String groupName) throws SQLException, Exception {
        return messageDAO.createGroupChat(groupName);
    }

    public void insertMember(int groupId, int memberId, String pos) throws SQLException, Exception {
        messageDAO.insertMember(groupId, memberId, pos);
    }

    public List<groupChat> getGroupChats(int userId) throws Exception {
        return messageDAO.getAllGroupChatsByUserId(userId);
    }

    public void renameGroup (int groupId, String newGroupName) {
        messageDAO.renameGroup(groupId, newGroupName);
    }

    public void saveGroupMessage(int userId, int groupId, String content) throws Exception {
        messageDAO.saveGroupMessage(userId, groupId, content);
    }

    public List<groupMessage> loadGroupMessages(int groupId) throws Exception {
        return messageDAO.loadGroupMessages(groupId);
    }

    public List<groupMessage> searchGroupMessages(int groupId, String searchText) throws Exception {
        return messageDAO.searchGroupMessages(groupId ,searchText);
    }
}
