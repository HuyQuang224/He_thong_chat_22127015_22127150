package view;

import model.User;
import controller.signUpController;
import javax.swing.*;
import java.awt.*;

public class Signup extends JFrame {

    private boolean isRegistrationInfoValid(String username, String fullName, String address, String dob, String gender, String email, String password, String confirmPassword) {
        // Kiểm tra các trường bắt buộc
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required!");
            return false;
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name is required!");
            return false;
        }
        if (address == null || address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address is required!");
            return false;
        }
        if (dob == null || dob.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date of Birth is required!");
            return false;
        }
        if (gender == null || gender.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Gender is required!");
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!");
            return false;
        }
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

        // Kiểm tra email hợp lệ
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return false;
        }

        // Kiểm tra ngày tháng hợp lệ (yyyy-MM-dd)
        if (!dob.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth!");
            return false;
        }

        return true; // Thông tin hợp lệ
    }

    public Signup() {
        setTitle("Sign up");
        setSize(600, 500);
        setResizable(false);
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        signUpController signUpController = new signUpController();

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        mainPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(usernameField, gbc);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        mainPanel.add(fullNameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(fullNameField, gbc);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        mainPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(addressField, gbc);

        // Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth:");
        JTextField dobField = new JTextField("YYYY-MM-DD");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        mainPanel.add(dobLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(dobField, gbc);

        // Gender
        JLabel genderLabel = new JLabel("Gender:");
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        mainPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(genderPanel, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.2;
        mainPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.2;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmpassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmpassField = new JPasswordField();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.2;
        mainPanel.add(confirmpassLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(confirmpassField, gbc);

        // Buttons
        JButton registerButton = new JButton("Sign up");
        JButton backToLoginButton = new JButton("Already have account? Log in");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        buttonPanel.add(backToLoginButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        registerButton.addActionListener(e -> {
            int id = 0;
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String address = addressField.getText();
            String dob = dobField.getText();
            String gender = "";
            if (maleButton.isSelected()){gender = "Male";}
            if (femaleButton.isSelected()){gender = "Female";}
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmpassField.getPassword());

            if (!isRegistrationInfoValid(username, fullName, address, dob, gender, email, password, confirmPassword)) {
                return; // Dừng lại nếu thông tin không hợp lệ
            }

            // Kiểm tra xem username hoặc email đã tồn tại chưa
            try {
                if (signUpController.checkUsernameExists(username)) {
                    JOptionPane.showMessageDialog(this, "Username already exists!");
                    return;
                }
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                if (signUpController.checkEmailExists(email)) {
                    JOptionPane.showMessageDialog(this, "Email already exists!");
                    return;
                }
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            boolean success;
            try {
                success = signUpController.registerUser(id, username, fullName, address, dob, gender, email, password, false, "Offline");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    dispose(); // Close the sign-up window
                    new Login().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed!");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            
        });

        backToLoginButton.addActionListener(e ->{
            dispose();
            new Login().setVisible(true);
        });


        add(mainPanel);
    }
}




