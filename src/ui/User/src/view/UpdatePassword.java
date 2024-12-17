package view;

import model.User;
import javax.swing.*;

import controller.logInController;
import controller.updatePasswordController;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdatePassword extends JFrame {

    private boolean isPasswordValid(String password, String confirmPassword) {
        // Kiểm tra các trường bắt buộc
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required!");
            return false;
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Confirm Password is required!");
            return false;
        }

        // Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return false;
        }

        return true; // Thông tin hợp lệ
    }

    public UpdatePassword(User user) {
        setTitle("Update Password");
        setSize(600, 200);
        setResizable(false);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Thêm WindowListener để xử lý trạng thái khi cửa sổ đóng
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Cập nhật trạng thái Offline khi đóng cửa sổ
                logInController logInController = new logInController();
                try {
                    logInController.updateStatus(user.getId(), "Offline");
                    System.exit(0); // Thoát chương trình
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // New Password
        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmpassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmpassField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        mainPanel.add(confirmpassLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(confirmpassField, gbc);

        // Update password button
        JButton updateButton = new JButton("Update password");
        JButton backToUserOption = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);
        buttonPanel.add(backToUserOption);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        updateButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmpassField.getPassword());

            if (!isPasswordValid(password, confirmPassword)) {
                return; // Dừng lại nếu thông tin không hợp lệ
            }

            updatePasswordController updatePasswordController = new updatePasswordController();
            user.setPassword(password);

            boolean success;
            try {
                success = updatePasswordController.updatePasword(user);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Update successful!");
                    dispose(); // Close the sign-up window
                    new UserOption(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed!");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backToUserOption.addActionListener(e -> {
            dispose();
            new UserOption(user).setVisible(true);
        });

        // Add ScrollPane to Frame
        add(mainPanel);
    }
}