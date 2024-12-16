package bus;

import dao.UserAccountDAO;
import datastructure.UserAccount;
import dto.EmailSender;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class UserAccountBus {
    private UserAccountDAO userAccountDAO;

    public UserAccountBus() {
        userAccountDAO = new UserAccountDAO();
    }

    public List<UserAccount> getAllUsers() {
        try {
            return userAccountDAO.getAllUsers();
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
            return null;
        }
    }

    public boolean addUser(UserAccount user) {
        try {
            return userAccountDAO.addUser(user);
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser(UserAccount user) {
        try {
            return userAccountDAO.updateUser(user);
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return userAccountDAO.deleteUser(userId);
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserBannedStatus(int userId, boolean isBanned) {
        return userAccountDAO.updateUserBannedStatus(userId, isBanned); 
    }

    public boolean updatePassword(int userId, String newPassword) {
        return userAccountDAO.updatePassword(userId, new UserAccount(newPassword));
    }

    public void resetPassword(String email) throws IOException {
        // Tạo mật khẩu ngẫu nhiên
        String newPassword = generateRandomPassword();

        // Cập nhật mật khẩu vào cơ sở dữ liệu
        if (userAccountDAO.updatePasswordByEmail(email, newPassword)) {
            // Gửi email thông báo mật khẩu mới
            sendPasswordResetEmail(email, newPassword);
        } else {
            throw new IOException("User not found or password update failed.");
        }
    }

    private String generateRandomPassword() {
        // Sử dụng các ký tự và số để tạo mật khẩu ngẫu nhiên
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {  // Độ dài mật khẩu là 8 ký tự
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    private void sendPasswordResetEmail(String email, String newPassword) throws IOException {
        // Gọi phương thức gửi email
        try {
            EmailSender.sendEmail(email, newPassword);
        } catch (Exception e) {
            throw new IOException("Error sending email: " + e.getMessage());
        }
    }
}
