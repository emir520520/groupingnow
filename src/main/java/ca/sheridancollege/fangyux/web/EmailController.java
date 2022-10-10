package ca.sheridancollege.fangyux.web;

import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.service.TopicService;
import ca.sheridancollege.fangyux.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@RestController
@AllArgsConstructor
public class EmailController {
    private void sendmail(String email) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("lambich1999@gmail.com", "cmftzoqqgiglmfgi");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("lambich1999@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Event information email");
        msg.setContent("Event information go here", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Event information go here", "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        Transport.send(msg);
    }

    @RequestMapping(value = "/sendemail")
    public String sendEmail(@AuthenticationPrincipal Authentication authentication) throws AddressException, MessagingException, IOException {
        //String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        sendmail(auth.getName());
        return "Email sent successfully";
    }
}
