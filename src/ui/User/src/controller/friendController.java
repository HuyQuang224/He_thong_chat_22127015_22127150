package controller;

import dao.friendDAO;
import dao.userDAO;
import model.User;

import java.util.List;

public class friendController {
    private friendDAO friendDAO;
    private userDAO userDAO;

    public friendController() {
        this.friendDAO = new friendDAO();
        this.userDAO = new userDAO();
    }

    public List<User> searchFriends(String username, String mainUsername, int mainId) {
        return friendDAO.searchFriends(username, mainUsername, mainId);
    }

    public List<User> searchUsers(String username, String mainUsername, int mainId) {
        return userDAO.searchUsers(username, mainUsername, mainId);
    }

    public boolean sendFriendRequest(int fromId, int toId) throws Exception {
        return friendDAO.sendFriendRequest(fromId, toId);
    }

    public List<User> getFriendRequests(int userId) throws Exception {
        return friendDAO.getFriendRequests(userId);
    }

    public boolean acceptFriendRequest(int userId, int requesterId) throws Exception {
        return friendDAO.acceptFriendRequest(userId, requesterId);
    }

    public boolean declineFriendRequest(int userId, int requesterId) throws Exception {
        return friendDAO.declineFriendRequest(userId, requesterId);
    }

    public boolean removeFriend(int userId, int friendId) throws Exception {
        return friendDAO.deleteFriend(userId, friendId);
    }

    public boolean blockFriend(int userId, int blockId) throws Exception {
        return friendDAO.blockUser(userId, blockId);
    }

    public List<User> getFriendList(int userId) throws Exception {
        return friendDAO.getFriendsByUserId(userId);
    }

    public List<User> getFilteredFriends(int userId, String filterName) throws Exception {
        return friendDAO.getFriendsByFilter(userId, filterName);
    }

    public List<User> getFilteredFriendRequest(int userId, String filterName) throws Exception {
        return friendDAO.getFilteredFriendRequests(userId, filterName);
    }

    public List<User> updateStatus(List<User> users) throws Exception {
        return friendDAO.updateFriendStatus(users);
    }

    public List<User> getBlockList(int userId) throws Exception {
        return friendDAO.getBlockList(userId);
    }

    public boolean cancelBlock(int userId, int blockId) throws Exception {
        return friendDAO.cancelBlock(userId, blockId);
    }

    public List<User> getFilteredOnlineFriends(int userId, String filterName) throws Exception {
        return friendDAO.getOnlineFriendsFilter(userId, filterName);
    }

    public List<User> getFriendsInGroup(int userId, int groupId) throws Exception {
        return friendDAO.getFriendsInGroup(userId, groupId);
    }
}
