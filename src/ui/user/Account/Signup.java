import javax.swing.*;
import java.awt.*;

public class Signup extends JFrame {

    public Signup() {
        setTitle("Sign up");
        setSize(600, 500);
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
        JTextField dobField = new JTextField("DD-MM-YYYY");
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
        JTextField passwordField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.2;
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(passwordField, gbc);

        // Confirm Password
        JLabel confirmpassLabel = new JLabel("Confirm Password:");
        JTextField confirmpassField = new JTextField();
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

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Signup::new);
    }
}




