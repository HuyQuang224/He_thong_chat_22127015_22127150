package ui.Chat;

import javax.swing.*;
import java.awt.*;

public class ChatHistory extends JFrame {

    public ChatHistory() {
        setTitle("Chat History");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Chat history area
        JTextArea chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        JScrollPane chatHistoryScroll = new JScrollPane(chatHistory);
        chatHistoryScroll.setBorder(BorderFactory.createTitledBorder("Chat History"));

        mainPanel.add(chatHistoryScroll, BorderLayout.CENTER);

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

        // Right buttons (Delete options)
        JButton deleteAllButton  = new JButton("Delete All Chat History");
        deleteAllButton.setBackground(Color.RED);
        deleteAllButton.setForeground(Color.WHITE);

        JButton deleteSelectedButton = new JButton("Delete Selected Messages");
        deleteSelectedButton.setBackground(Color.RED);
        deleteSelectedButton.setForeground(Color.WHITE);

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(deleteSelectedButton);
        rightButtonPanel.add(deleteAllButton);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Add button panel to the top panel
        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add top panel to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Search panel at the bottom
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 1, 5, 5));

        // Search with one person
        JPanel searchChatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchChatLabel = new JLabel("Search in chat:");
        JTextField searchChatField = new JTextField(20);
        JButton searchChatButton = new JButton("Search");
        searchChatPanel.add(searchChatLabel);
        searchChatPanel.add(searchChatField);
        searchChatPanel.add(searchChatButton);

        // Add search chat panel to search panel
        searchPanel.add(searchChatPanel);

        // Add search panel to main panel
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatHistory::new);
    }
}

