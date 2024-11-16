import javax.swing.*;
import java.awt.*;

public class GroupChatHistory extends JFrame {

    public GroupChatHistory() {
        setTitle("Group Chat History");
        setSize(600, 500);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Top panel for buttons and status label (back button, delete buttons)
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

        // Delete all button
        JButton deleteAllButton = new JButton("Delete All Chat History");
        deleteAllButton.setBackground(Color.RED);
        deleteAllButton.setForeground(Color.WHITE);

        // Delete selected button
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
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(topPanel, gbc);

        // Chat history area
        JTextArea chatHistory = new JTextArea();
        chatHistory.setEditable(false);
        chatHistory.setLineWrap(true);
        chatHistory.setWrapStyleWord(true);
        JScrollPane chatHistoryScroll = new JScrollPane(chatHistory);
        chatHistoryScroll.setBorder(BorderFactory.createTitledBorder("Group Chat History"));
        
        // Add chat history scroll pane to the main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(chatHistoryScroll, gbc);

        // Search panel at the bottom (search text and button)
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(1, 1, 5, 5));

        // Search chat
        JPanel searchChatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchChatLabel = new JLabel("Search in group chat:");
        JTextField searchChatField = new JTextField(20);
        JButton searchChatButton = new JButton("Search");
        searchChatPanel.add(searchChatLabel);
        searchChatPanel.add(searchChatField);
        searchChatPanel.add(searchChatButton);

        // Add search chat panel to search panel
        searchPanel.add(searchChatPanel);

        // Add search panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchPanel, gbc);

        add(mainPanel);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GroupChatHistory::new);
    }
}
