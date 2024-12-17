package controller;

import dao.userDAO;
import model.User;

public class signUpController {
    private userDAO userDAO;

    public signUpController() {
        this.userDAO = new userDAO();
    }

    public boolean registerUser(int id ,String username, String fullName, String address, String dob, String gender, String email, String password, boolean banned, String status) throws Exception {
        User user = new User(id, username, fullName, address, dob, gender, email, password, banned, status);
        return userDAO.registerUser(user);
    }

    public boolean checkUsernameExists(String username) throws Exception {
        return userDAO.isUsernameExists(username);
    }

    public boolean checkEmailExists(String email) throws Exception {
        return userDAO.isEmailExists(email);
    }
}
