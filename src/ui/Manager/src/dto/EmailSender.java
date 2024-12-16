package dto;

import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class EmailSender {
    public static void sendEmail(String recipientEmail, String newPassword) throws IOException {
        String smtpServer = "smtp.gmail.com";
        int smtpPort = 587;
        String fromEmail = "sfeddsgaming@gmail.com";  // Email của bạn
        String fromPassword = "200422qA@";           // Mật khẩu ứng dụng của email bạn

        // Kết nối tới máy chủ SMTP
        Socket socket = new Socket(smtpServer, smtpPort);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Đọc phản hồi chào mừng từ máy chủ SMTP
        System.out.println("S: " + reader.readLine());

        // Bắt đầu giao thức STARTTLS
        sendCommand(writer, reader, "EHLO " + smtpServer);
        sendCommand(writer, reader, "STARTTLS");

        // Thiết lập lớp bảo mật
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket, smtpServer, smtpPort, true);
        reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

        // Lặp lại EHLO sau khi bắt đầu STARTTLS
        sendCommand(writer, reader, "EHLO " + smtpServer);

        // Gửi thông tin xác thực
        sendCommand(writer, reader, "AUTH LOGIN");
        sendCommand(writer, reader, Base64.getEncoder().encodeToString(fromEmail.getBytes()));
        sendCommand(writer, reader, Base64.getEncoder().encodeToString(fromPassword.getBytes()));

        // Thiết lập thông tin email
        sendCommand(writer, reader, "MAIL FROM:<" + fromEmail + ">");
        sendCommand(writer, reader, "RCPT TO:<" + recipientEmail + ">");
        sendCommand(writer, reader, "DATA");
        writer.write("Subject: Password Reset\r\n");
        writer.write("To: " + recipientEmail + "\r\n");
        writer.write("From: " + fromEmail + "\r\n");
        writer.write("\r\n");
        writer.write("Your new password is: " + newPassword + "\r\n");
        writer.write(".\r\n");
        writer.flush();
        System.out.println("S: " + reader.readLine());

        // Đóng kết nối
        sendCommand(writer, reader, "QUIT");
        reader.close();
        writer.close();
        sslSocket.close();
    }

    // Phương thức tiện ích để gửi lệnh và nhận phản hồi từ máy chủ SMTP
    private static void sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        System.out.println("C: " + command);
        System.out.println("S: " + reader.readLine());
    }
}
