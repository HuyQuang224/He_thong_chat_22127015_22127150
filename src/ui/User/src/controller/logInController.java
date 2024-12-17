package controller;

import dao.userDAO;
import model.User;

public class logInController {
    private userDAO userDAO;

    public logInController() {
        this.userDAO = new userDAO();
    }

    public User authenticateUser(String identifier, String password) throws Exception {
        User user = userDAO.getUserByUsernameOrEmailAndPassword(identifier, password);
        if (user != null) {
            // Cập nhật trạng thái online
            if (user.getBanned() == false) {
                if (user.getStatus().equals("isUsed")) {
                    return user;
                }
                userDAO.updateOnlineStatus(user.getId(), "Online");
            }
        }
        return user;
    }

    public void updateStatus(int id, String status) throws Exception {
        userDAO.updateOnlineStatus(id, status);
    }

}

