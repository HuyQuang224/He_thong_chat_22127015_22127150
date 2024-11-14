package ui.Chat;

import javax.swing.*;
import java.awt.*;

public class ChatUI extends JFrame {

    public ChatUI() {
        setTitle("Chat Interface");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top panel for buttons and status label
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Button panel for the top buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Left button (Back)
        JButton backButton = new JButton("Back");
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(backButton);
        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Right buttons (Report Spam and View Chat History)
        JButton reportSpamButton = new JButton("Report Spam");
        reportSpamButton.setBackground(Color.RED);
        reportSpamButton.setForeground(Color.WHITE);

        JButton viewHistoryButton = new JButton("Chat History");

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(viewHistoryButton);
        rightButtonPanel.add(reportSpamButton);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Add button panel to the top panel
        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // Status label
        JLabel statusLabel = new JLabel("Status: Online");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add status label to the top panel
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add top panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Chat area (center)
        JTextArea chatFrame = new JTextArea();
        chatFrame.setEditable(false);
        chatFrame.setLineWrap(true);
        chatFrame.setWrapStyleWord(true);
        JScrollPane chatFrameScroll = new JScrollPane(chatFrame);
        chatFrameScroll.setBorder(BorderFactory.createTitledBorder("Username of Friend"));

        mainPanel.add(chatFrameScroll, BorderLayout.CENTER);

        // Chat input panel (bottom)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JTextField messageInput = new JTextField();
        messageInput.setBorder(BorderFactory.createTitledBorder("Enter Message"));
        JButton sendButton = new JButton("Send");

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Adding main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatUI::new);
    }
}




