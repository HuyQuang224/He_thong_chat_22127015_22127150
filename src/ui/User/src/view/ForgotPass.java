package view;
import javax.swing.*;

import controller.ForgotPasswordController;
import java.awt.*;

public class ForgotPass extends JFrame {

    public ForgotPass() {
        setTitle("Email Validation");
        setSize(500, 150);
        setResizable(false);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        mainPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        mainPanel.add(emailField, gbc);

        // Buttons
        JButton sendButton = new JButton("Send password to email");
        JButton backToLoginButton = new JButton("Go to log in");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(sendButton);
        buttonPanel.add(backToLoginButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        sendButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            ForgotPasswordController controller = new ForgotPasswordController();

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your email!");
                return;
            }

            boolean result = controller.resetPassword(email);

            if (result) {
                JOptionPane.showMessageDialog(this, "A new password has been sent to your email!");
            } else {
                JOptionPane.showMessageDialog(this, "Email not found or failed to send email. Please try again.");
            }
        });

        backToLoginButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });
        add(mainPanel);
    }
}




