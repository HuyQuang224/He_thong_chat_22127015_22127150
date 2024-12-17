package controller;

import model.User;
import dao.userDAO;

public class updatePasswordController {
    private userDAO userDAO;

    public updatePasswordController() {
        userDAO = new userDAO();
    }

    public boolean updatePasword(User user){
        return userDAO.UpdatePassword(user);
    }
}
