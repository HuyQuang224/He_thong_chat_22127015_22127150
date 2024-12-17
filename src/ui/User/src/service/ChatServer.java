package service;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import javax.swing.*;
import java.awt.event.*;

import controller.chatController;

public class ChatServer {
    private static final int PORT = 12345;
    private static ConcurrentHashMap<String, PrintWriter> userWriters = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, CopyOnWriteArrayList<String>> groupMembers = new ConcurrentHashMap<>();
    private static chatController chatController = new chatController();
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        // Hiển thị GUI thông báo server
        SwingUtilities.invokeLater(() -> createServerGUI());

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("Server stopped.");
        }
    }

    private static void createServerGUI() {
        JFrame frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Ngăn mặc định đóng cửa sổ
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Server is running on port " + PORT, SwingConstants.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to stop the server?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (serverSocket != null && !serverSocket.isClosed()) {
                            serverSocket.close();
                        }
                        System.exit(0); // Thoát chương trình
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.add(label);
        frame.setVisible(true);
    }

    public static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String userId;
        private String userName;
        private String toUserId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                userId = in.readLine();
                userName = in.readLine().trim();
                toUserId = in.readLine();

                userWriters.put(userId, out);

                if (toUserId.charAt(0) == 'G') {
                    groupMembers.putIfAbsent(toUserId, new CopyOnWriteArrayList<>());
                    if (!groupMembers.get(toUserId).contains(userId)) {
                        groupMembers.get(toUserId).add(userId);
                        System.out.println("User " + userName + " joined group " + toUserId);
                    }
                }

                System.out.println("User " + userId + " (" + userName + ") connected.");

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(userName + ": " + message);
                    try {
                        handleMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                System.out.println("User " + userName + " disconnected.");
            } finally {
                cleanup();
            }
        }

        private void handleMessage(String message) throws Exception {
            if (toUserId.charAt(0) == 'G') {
                broadcastToGroup(toUserId, userName + ": " + message);
                int groupId = Integer.parseInt(toUserId.substring(1));
                chatController.saveGroupMessage(Integer.parseInt(userId), groupId, userName + ": " + message);
            } else {
                PrintWriter recipientWriter = userWriters.get(toUserId);
                if (recipientWriter != null) {
                    recipientWriter.println(userName + ": " + message);
                }
                int recipientId = Integer.parseInt(toUserId);
                chatController.saveMessage(Integer.parseInt(userId), recipientId, userName + ": " + message);
            }
        }

        private void broadcastToGroup(String groupId, String message) {
            CopyOnWriteArrayList<String> members = groupMembers.get(groupId);
            if (members != null) {
                for (String memberId : members) {
                    if (!memberId.equals(userId)) {
                        PrintWriter writer = userWriters.get(memberId);
                        if (writer != null) {
                            writer.println(message);
                        }
                    }
                }
            }
        }

        private void cleanup() {
            if (userId != null) {
                userWriters.remove(userId);
            }

            if (toUserId != null && toUserId.charAt(0) == 'G') {
                CopyOnWriteArrayList<String> members = groupMembers.get(toUserId);
                if (members != null) {
                    members.remove(userId);
                    if (members.isEmpty()) {
                        groupMembers.remove(toUserId);
                    }
                }
            }

            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
