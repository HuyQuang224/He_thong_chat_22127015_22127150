package view;

import model.User;
import controller.logInController;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    public Login() {
        setTitle("Log in");
        setSize(600, 250); 
        setResizable(false); 

        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel usernameOrEmailLabel = new JLabel("Username or Email:");
        JTextField usernameOrEmailField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        mainPanel.add(usernameOrEmailLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(usernameOrEmailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(passwordField, gbc);

        // Login Buttons
        JButton loginButton = new JButton("Log in");
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginPanel, gbc);

        // Other Buttons
        JButton forgotPassButton = new JButton("Forgot password?");
        JButton backToSignupButton = new JButton("Don't have an account? Sign up");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(forgotPassButton);
        buttonPanel.add(backToSignupButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String identifier = usernameOrEmailField.getText();
            String password = new String(passwordField.getPassword());

            logInController controller = new logInController();
            User user;
            try {
                user = controller.authenticateUser(identifier, password);
                if (user != null) {
                    if (user.getBanned() == false) {
                        if (user.getStatus().equals("isUsed")) {
                            JOptionPane.showMessageDialog(this, "Your account is in use!");
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "Login successful!");
                        // Chuyển sang giao diện chính
                        dispose();
                        new UserOption(user).setVisible(true);;
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Your account has been banned!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username/email or password!");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backToSignupButton.addActionListener(e -> {
            dispose();
            new Signup().setVisible(true);
        });

        forgotPassButton.addActionListener(e -> {
            dispose();
            new ForgotPass().setVisible(true);
        });

        add(mainPanel);
    }
}




