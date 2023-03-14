package security;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import utility.ApplicationResourcesUtil;

public class ResetMailer {

    public static void main(String[] args) {

        ResetMailer resetMailer = new ResetMailer();
        String href = "http://localhost:8080/familyPhotos/resetPasswordProcess?email="
        		+ "demo@somewhere.com&token=sYrQ4_V57CYgnFzSk5kBu8xIod8";
        String link = "click <a href='" + href + "' >here</a> to reset password";
        resetMailer.sendMail("demo@somewhere.com", "Password Reset", link);
    }
    
    public void sendMail(String toAddress, String subject, String htmlContent) {
    	
    	Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(ApplicationResourcesUtil.SMTP_USERNAME, ApplicationResourcesUtil.SMTP_PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(ApplicationResourcesUtil.SMTP_USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toAddress)
            );
            message.setSubject(subject);

//                        message.setText("test5");            

            // html content
            BodyPart mimeBody = new MimeBodyPart();
            mimeBody.setContent(htmlContent, "text/html");
            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(mimeBody);
            message.setContent(multiPart);

            Transport.send(message);

            System.out.println("ResetMailer has sent email to "+toAddress);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    	
    }

}