import javax.swing.*;
import java.awt.*;

public class UserOption extends JFrame {

    public UserOption() {
        setTitle("User Options");
        setSize(300, 300);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MainPanel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username label
        JLabel userNameLabel = new JLabel("User Name: HuyQuang123");

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

        // Log out button
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(logoutButton, gbc);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserOption::new);
    }
}
