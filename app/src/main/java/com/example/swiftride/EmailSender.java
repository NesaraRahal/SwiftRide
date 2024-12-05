package com.example.swiftride;

import android.os.AsyncTask;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    private String recipientEmail;
    private String subject;
    private String body;

    public EmailSender(String recipientEmail, String subject, String body) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
    }

    public void sendEmail() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    sendMail();
                    return true; // Email sent successfully
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return false; // Email sending failed
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    System.out.println("Email sent successfully.");
                } else {
                    System.out.println("Failed to send email.");
                }
            }
        }.execute();
    }

    private void sendMail() throws MessagingException {
        final String username = "developer.udithsandaruwan@gmail.com"; // Replace with your email
        final String password = "aadg hjnq imzr onmq"; // Replace with your app password

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
