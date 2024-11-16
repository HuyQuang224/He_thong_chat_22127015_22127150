import javax.swing.*;
import java.awt.*;

public class ChatUI extends JFrame {

    public ChatUI() {
        setTitle("Chat Interface");
        setSize(600, 400);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Top panel for buttons and status label
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Button panel for the top buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        // Back button
        JButton backButton = new JButton("Back");
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtonPanel.add(backButton);
        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);

        // Report Spam button
        JButton reportSpamButton = new JButton("Report Spam");
        reportSpamButton.setBackground(Color.RED);
        reportSpamButton.setForeground(Color.WHITE);

        // View Chat History button
        JButton viewHistoryButton = new JButton("Chat History");

        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtonPanel.add(viewHistoryButton);
        rightButtonPanel.add(reportSpamButton);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);

        // Add button panel to the top panel
        topPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add top panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(topPanel, gbc);

        // Chat area
        JTextArea chatFrame = new JTextArea();
        chatFrame.setEditable(false);
        chatFrame.setLineWrap(true);
        chatFrame.setWrapStyleWord(true);
        chatFrame.setBorder(BorderFactory.createTitledBorder("Username of Friend"));

        // Add chat area to main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(chatFrame, gbc);

        // Chat input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JTextField messageInput = new JTextField();
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
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatUI::new);
    }
}
