package controller;

import dao.userDAO;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ForgotPasswordController {
    private userDAO userDAO;

    public ForgotPasswordController() {
        userDAO = new userDAO();
    }

    // Generate a random password
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    // Send email
    private void sendEmail(String recipient, String newPassword) throws MessagingException {
        // pass: axkyrosvvbsjwpgu
        String sender = "huyquangtest1@gmail.com"; // Replace with your email
        String senderPassword = "axkyrosvvbsjwpgu"; // Replace with your email's password
        String subject = "Password Reset";
        String body = "Your new password is: " + newPassword;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    // Reset password and send to email
    public boolean resetPassword(String email) {
        String newPassword = generateRandomPassword();
        boolean isUpdated = userDAO.updatePasswordByEmail(email, newPassword);

        if (isUpdated) {
            try {
                sendEmail(email, newPassword);
                return true;
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

