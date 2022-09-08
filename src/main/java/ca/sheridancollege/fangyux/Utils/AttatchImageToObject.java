package ca.sheridancollege.fangyux.Utils;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

public class AttatchImageToObject {

    public static User attatchToUser(User user, MultipartFile image){
        user.setPhoto(transfer(image));

        return user;
    }

    public static SchoolGroup attatchToGroup(SchoolGroup group, MultipartFile image){
        group.setPhoto(transfer(image));

        return group;
    }

    public static Event attatchToEvent(Event event, MultipartFile image){
        event.setEventImage(transfer(image));

        return event;
    }

    private static byte[] transfer(MultipartFile image){
        Blob blob = null;
        byte[] blobAsBytes=null;
        try {
            blob = new SerialBlob(image.getBytes());

            int blobLength = (int) blob.length();
            blobAsBytes = blob.getBytes(1, blobLength);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blobAsBytes;
    }
}