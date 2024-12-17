package view;

import model.User;
import model.Message;
import service.ChatClient;
import controller.chatController;
import controller.logInController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChatUI extends JFrame {

    private JTextArea chatFrame;
    private JTextField messageInput;
    private ChatClient chatClient;
    private chatController chatController;
    User user;
    String toUsername;

    public ChatUI(User user, int toUserId, String toUsername) throws Exception {
        setTitle("Chat " + "with " + toUsername);
        setSize(600, 400);
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

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back button
        JButton backButton = new JButton("Back to Friend List");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(backButton, gbc);

        // Report button
        JButton reportButton = new JButton("Report Spam");
        reportButton.setBackground(Color.RED);
        reportButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(reportButton, gbc);

        // Report button
        JButton viewHistoryButton = new JButton("View History");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(viewHistoryButton, gbc);

        // Chat area
        chatFrame = new JTextArea();
        chatFrame.setEditable(false);
        chatFrame.setLineWrap(true);
        chatFrame.setWrapStyleWord(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(chatFrame, gbc);

        // Add chat area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(chatFrame);
        

        // Add scroll pane to main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        // Chat input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageInput = new JTextField();
        messageInput.setBorder(BorderFactory.createTitledBorder("Enter Message"));
        JButton sendButton = new JButton("Send");

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add input panel to main panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel, gbc);

        add(mainPanel);

        chatController = new chatController();
        loadMessage(chatController.loadMessage(user.getId(), toUserId));

        // Initialize chat client
        chatClient = new ChatClient();
        try {
            chatClient.connect(user.getId(), user.getUsername(), toUserId);
            new ChatClient.MessageListener(chatClient, this).start(); // Truyền tham chiếu UI vào MessageListener
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send button functionality
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageInput.getText();
                if (!message.isEmpty()) {
                    chatClient.sendMessage(message);  // Send message to server
                    appendMessage(user.getUsername() + ": " + message); // Hiển thị tin nhắn của bạn trên UI
                    messageInput.setText("");
                }
            }
        });

        reportButton.addActionListener(e -> {
            dispose();
            try {
                new reportSpam(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        viewHistoryButton.addActionListener(e -> {
            dispose();
            try {
                new ChatHistory(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public ChatUI(User user, int toUserId, String toUsername, int messageId) throws Exception {
        setTitle("Chat " + "with " + toUsername);
        setSize(600, 400);
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

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Back button
        JButton backButton = new JButton("Back to Friend List");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(backButton, gbc);

        // Report button
        JButton reportButton = new JButton("Report Spam");
        reportButton.setBackground(Color.RED);
        reportButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(reportButton, gbc);

        // Report button
        JButton viewHistoryButton = new JButton("View History");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(viewHistoryButton, gbc);

        // Chat area
        chatFrame = new JTextArea();
        chatFrame.setEditable(false);
        chatFrame.setLineWrap(true);
        chatFrame.setWrapStyleWord(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(chatFrame, gbc);

        // Add chat area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(chatFrame);
        

        // Add scroll pane to main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        // Chat input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageInput = new JTextField();
        messageInput.setBorder(BorderFactory.createTitledBorder("Enter Message"));
        JButton sendButton = new JButton("Send");

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add input panel to main panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(inputPanel, gbc);

        add(mainPanel);

        this.user = user;
        this.toUsername = toUsername;

        chatController = new chatController();
        List<Message> allMessages = chatController.loadMessage(user.getId(), toUserId);
        loadMessage(allMessages);
        String chatContent = buildChatContent(allMessages);

        scrollToMessage(chatFrame, chatContent, messageId);

        // Initialize chat client
        chatClient = new ChatClient();
        try {
            chatClient.connect(user.getId(), user.getUsername(), toUserId);
            new ChatClient.MessageListener(chatClient, this).start(); // Truyền tham chiếu UI vào MessageListener
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send button functionality
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageInput.getText();
                if (!message.isEmpty()) {
                    chatClient.sendMessage(message);  // Send message to server
                    appendMessage(user.getUsername() + ": " + message); // Hiển thị tin nhắn của bạn trên UI
                    messageInput.setText("");
                }
            }
        });

        reportButton.addActionListener(e -> {
            dispose();
            try {
                new reportSpam(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        viewHistoryButton.addActionListener(e -> {
            dispose();
            try {
                new ChatHistory(user, toUserId, toUsername).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            try {
                new FriendList(user).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    private void scrollToMessage(JTextArea chatArea, String chatContent, int messageId) {
        // Tìm vị trí của tin nhắn trong nội dung
        int targetPosition = messageId;
        if (targetPosition != -1) {
            try {
                // Đặt con trỏ vào vị trí tin nhắn
                chatArea.setCaretPosition(targetPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Message not found.");
        }
    }

    public void loadMessage(List<Message> messages) {
        for (Message message : messages) {
            appendMessage(message.getContent());
        }
    }

    private String buildChatContent(List<Message> messages) {
        StringBuilder chatBuilder = new StringBuilder();
    
        for (Message message : messages) {
            chatBuilder.append(message.getFromUser() == user.getId() ? user.getUsername() : toUsername)
                       .append(": ")
                       .append(message.getContent())
                       .append("\n");
        }
    
        return chatBuilder.toString();
    }

    // Phương thức hiển thị tin nhắn nhận được
    public void appendMessage(String message) {
        chatFrame.append(message + "\n");
        chatFrame.setCaretPosition(chatFrame.getDocument().getLength()); // Cuộn xuống cuối mỗi khi có tin nhắn mới
    }
}