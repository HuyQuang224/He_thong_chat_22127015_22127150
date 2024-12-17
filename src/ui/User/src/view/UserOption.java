package view;

import model.User;
import javax.swing.*;

import controller.logInController;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserOption extends JFrame {

    public UserOption(User user) {
        setTitle("User Options");
        setSize(300, 300);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        // MainPanel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label
        JLabel userNameLabel = new JLabel("Username: " + user.getUsername());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(userNameLabel, gbc);

        // Update Info button
        JButton updateInfoButton = new JButton("Update Info");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(updateInfoButton, gbc);

        // Update Password button
        JButton updatePasswordButton = new JButton("Update Password");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(updatePasswordButton, gbc);

        // Friend List button
        JButton friendListButton = new JButton("Friend List");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(friendListButton, gbc);

        // Group chat button
        JButton groupChatButton = new JButton("Group Chat");

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(groupChatButton, gbc);

        // Log out button
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(logoutButton, gbc);

        updateInfoButton.addActionListener(e -> {
            dispose();
            new UpdateInfo(user).setVisible(true);;
        });

        updatePasswordButton.addActionListener(e -> {
            dispose();
            new UpdatePassword(user).setVisible(true);;
        });

        friendListButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        groupChatButton.addActionListener(e -> {
            dispose();
            try {
                new groupChatList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        // Nút Logout
        logInController logInController = new logInController();
        logoutButton.addActionListener(e -> {
            try {
                logInController.updateStatus(user.getId(), "Offline");
                dispose(); // Thoát chương trình
                new Login().setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(mainPanel);
    }
}
