package controller;

import model.User;
import dao.userDAO;

public class updateInfoController {
    private userDAO userDAO;

    public updateInfoController() {
        userDAO = new userDAO();
    }

    public boolean updateInfo(User user){
        return userDAO.updateInfo(user);
    }

    public boolean checkUpUsernameExists(String username, int id) throws Exception {
        return userDAO.isUpUsernameExists(username, id);
    }

    public boolean checkUpEmailExists(String email, int id) throws Exception {
        return userDAO.isUpEmailExists(email, id);
    }
}
