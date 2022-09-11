package ca.sheridancollege.fangyux.Utils;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Base64;

public class ImageOperation {

    public static User attatchToUser(User user, MultipartFile image){
        user.setPhoto(transferToBytes(image));

        return user;
    }

    public static SchoolGroup attatchToGroup(SchoolGroup group, MultipartFile image){
        group.setPhoto(transferToBytes(image));

        return group;
    }

    public static Event attatchToEvent(Event event, MultipartFile image){
        event.setEventImage(transferToBytes(image));

        return event;
    }

    public static User attatchBase64ToUser(User user) throws UnsupportedEncodingException {
        user.setBase64Encoded(transferToBase64(user.getPhoto()));

        return user;
    }

    public static SchoolGroup attatchBase64ToGroup(SchoolGroup group) throws UnsupportedEncodingException {
        group.setBase64Encoded(transferToBase64(group.getPhoto()));

        return group;
    }

    public static Event attatchBase64ToEvent(Event event) throws UnsupportedEncodingException {
        event.setBase64Encoded(transferToBase64(event.getEventImage()));

        return event;
    }

    private static byte[] transferToBytes(MultipartFile image){
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

    public static String transferToBase64(byte[] photo) throws UnsupportedEncodingException {
        byte[] encodeBase64 = Base64.getEncoder().encode(photo);

        return new String(encodeBase64, "UTF-8");
    }
}