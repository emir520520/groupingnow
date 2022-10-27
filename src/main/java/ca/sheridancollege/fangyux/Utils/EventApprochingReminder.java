package ca.sheridancollege.fangyux.Utils;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.CartEventRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class EventApprochingReminder {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private CartEventRepository cartEventRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JavaMailSender mailSender;

//    @Scheduled(cron = "0 */1 * ? * *")
//@Scheduled(cron = "0 0 7 1/1 * ? *")

    //7:00am every day
    @Scheduled(cron = "0 */1 * ? * *")
    public void sendEventReminderEmail() throws MessagingException, UnsupportedEncodingException {
        //Get the events that approaches
        List<Long> eventIDs=eventRepo.getEventsApproching();

        //For every event, get all users that registered in the event
        for(int i=0; i<eventIDs.size();i++){
            Event event=eventRepo.getEventById(eventIDs.get(i));

            if(event.getRemindered().compareTo("true")!=0){
                //Get user IDs of the event
                List<Long> userIDs=cartEventRepo.getUsersByEventID(eventIDs.get(i));

                for(int k=0;k<userIDs.size();k++){
                    //Send the reminder email
                    sendReminderEmail(userIDs.get(k), event);
                }

                eventRepo.setEventReminderedToTrue(eventIDs.get(i));
            }
        }
    }

    public void sendReminderEmail(Long userID, Event event)
            throws MessagingException, UnsupportedEncodingException {
        //Get the user object
        User user=userRepo.findUserById(userID);

        String toAddress = user.getEmail();
        String fromAddress = "applicationgroupnow@gmail.com";
        String senderName = "Grouping Now Team";
        String subject = "Your event is approaching";
        String content = "Dear [[name]],<br>"
                + "The event you registered before: [[event_name]] will be held in two hours.  Please get ready for it!<br><br><br>"
                + "Thank you,<br>"
                + "Grouping Now team.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() +" "+ user.getLastName());
        content=content.replace("[[event_name]]", event.getEventName());

        helper.setText(content, true);

        mailSender.send(message);
    }
}