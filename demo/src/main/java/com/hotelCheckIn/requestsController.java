package com.hotelCheckIn;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.*;

public class requestsController {
    
    public void initialize()
    {

    }

        public void sendEmail()
    {
        // email credentials and message data
        final String username = "4dragonsresort@gmail.com";
        final String password = "kbbd dmgx cqmg zewf";
        final String recipient = "kerolosharby@gmail.com";
        String subject = "Hello world!";
        String body = "This is a test message from Java mail. If you see this then it's working.";

        // add mail server/encryption properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // create a new email session
        Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
        Session session = Session.getInstance(properties, auth);

        // send the email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("adminController.sendEmail(): Email sent sucessfully!");
        } catch(MessagingException e) {
            System.out.println("adminController.sendEmail(): MessagingException");
            e.printStackTrace();
        }
    }
}
