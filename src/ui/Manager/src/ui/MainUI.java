package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainUI {

    private JFrame frame;

    public MainUI() {
        // Khởi tạo JFrame
        frame = new JFrame("Main UI");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel chính
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); 

        // Thêm tiêu đề
        JLabel titleLabel = new JLabel("Main Navigation Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(34, 34, 112)); 
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(40)); 

        // Tạo các nút với giao diện đẹp
        panel.add(createButton("Go to User Account Management", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserAccountUI userAccountUI = new UserAccountUI();
                userAccountUI.showUI();
            }
        }));

        panel.add(Box.createVerticalStrut(30)); 

        panel.add(createButton("Go to New User UI", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewUserUI newUserUI = new NewUserUI();
                newUserUI.showUI();
            }
        }));

        panel.add(Box.createVerticalStrut(30)); 

        panel.add(createButton("Go to Group Chat Management UI", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupChatManagementUI groupChatUI = new GroupChatManagementUI();
                groupChatUI.showUI();
            }
        }));

        panel.add(Box.createVerticalStrut(30)); 

        panel.add(createButton("Go to User Friend Management UI", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserFriendUI userFriendUI = new UserFriendUI();
                userFriendUI.showUI();
            }
        }));

        panel.add(Box.createVerticalStrut(30)); 

        panel.add(createButton("Go to User Login History UI", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserLoginHistoryUI userLoginHistoryUI = new UserLoginHistoryUI();
                userLoginHistoryUI.showUI();
            }
        }));

        panel.add(Box.createVerticalStrut(30)); 

        // Thêm nút chuyển đến SpamReportUI
        panel.add(createButton("Go to Spam Report UI", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpamReportUI spamReportUI = new SpamReportUI();
                spamReportUI.showUI();
            }
        }));

        // Thêm panel vào frame
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        // Hiển thị JFrame
        frame.setVisible(true);
    }

    /**
     * Tạo một nút với giao diện và hành động tùy chỉnh.
     */
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        button.setFont(new Font("Arial", Font.PLAIN, 18)); 
        button.setBackground(new Color(70, 130, 180)); 
        button.setForeground(Color.WHITE); 
        button.setFocusPainted(false); 
        button.setMaximumSize(new Dimension(300, 40)); 
        button.addActionListener(actionListener);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI());
    }
}
