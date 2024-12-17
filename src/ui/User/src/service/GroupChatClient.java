package service;

import java.io.*;
import java.net.*;

import view.GroupChatUI;

public class GroupChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Server's address
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(int UserId, String userName, int groupId) throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Gửi userId và tên đến server
        out.println(UserId);
        out.println(userName);
        out.println("G" + groupId);

        System.out.println("Connected to server as user " + UserId + " (" + userName + ")");
    }

    public void sendMessage(String message) {
        out.println(message); // Send message to server
    }

    public String receiveMessage() throws IOException {
        return in.readLine(); // Receive message from server
    }

    public void close() throws IOException {
        sendMessage("EXIT"); // Gửi thông báo thoát (tuỳ chọn)
        in.close();
        out.close();
        socket.close();
    }

    // For handling receiving messages on a separate thread
    public static class MessageListener extends Thread {
        private GroupChatClient groupChatClient;
        private GroupChatUI groupChatUI; // Tham chiếu đến ChatUI
    
        public MessageListener(GroupChatClient groupChatClient, GroupChatUI groupChatUI) {
            this.groupChatClient = groupChatClient;
            this.groupChatUI = groupChatUI; // Lưu tham chiếu đến UI
        }
    
        @Override
        public void run() {
            try {
                String message;
                while ((message = groupChatClient.receiveMessage()) != null) {
                    System.out.println("Received: " + message);
                    groupChatUI.appendMessage(message); // Cập nhật UI với tin nhắn nhận được
                }
            } catch (IOException e) {
                System.out.println("Connection to server lost.");
            }
        }
    }

    
}
