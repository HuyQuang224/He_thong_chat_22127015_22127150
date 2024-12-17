package view;

import model.User;
import controller.logInController;
import controller.updateInfoController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateInfo extends JFrame {

    private boolean isUpdateInfoValid(String username, String fullName, String address, String dob, String gender, String email) {
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

    public UpdateInfo(User user) {
        setTitle("Update Info");
        setSize(600, 400);
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

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(user.getUsername());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        mainPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(usernameField, gbc);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name:");
        JTextField fullNameField = new JTextField(user.getFullName());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        mainPanel.add(fullNameLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(fullNameField, gbc);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(user.getAddress());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        mainPanel.add(addressLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(addressField, gbc);

        // Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth:");
        JTextField dobField = new JTextField(user.getDob());
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
        if (user.getGender().equals("Male")){maleButton.setSelected(true);}
        else {femaleButton.setSelected(true);}
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
        JTextField emailField = new JTextField(user.getEmail());
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.2;
        mainPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(emailField, gbc);

        // Update button
        JButton updateButton = new JButton("Update");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        updateInfoController updateInfoController = new updateInfoController();
        updateButton.addActionListener(e -> {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String address = addressField.getText();
            String dob = dobField.getText();
            String gender = "";
            if (maleButton.isSelected()){gender = "Male";}
            if (femaleButton.isSelected()){gender = "Female";}
            String email = emailField.getText();

            if (!isUpdateInfoValid(username, fullName, address, dob, gender, email)) {
                return; // Dừng lại nếu thông tin không hợp lệ
            }

            // Kiểm tra xem username hoặc email đã tồn tại chưa
            try {
                if (updateInfoController.checkUpUsernameExists(username, user.getId())) {
                    JOptionPane.showMessageDialog(this, "Username already exists!");
                    return;
                }
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                if (updateInfoController.checkUpEmailExists(email, user.getId())) {
                    JOptionPane.showMessageDialog(this, "Email already exists!");
                    return;
                }
            } catch (HeadlessException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            user.setUsername(username);
            user.setFullName(fullName);
            user.setAddress(address);
            user.setDob(dob);
            user.setGender(gender);
            user.setEmail(email);

            boolean success;
            try {
                success = updateInfoController.updateInfo(user);
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


        add(mainPanel);
    }
}





