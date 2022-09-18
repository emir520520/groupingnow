package ca.sheridancollege.fangyux.Utils;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
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

    public static User attatchBase64ToUser(User user) throws IOException {
        user.setBase64Encoded(transferToBase64(user.getPhoto()));

        return user;
    }

    public static SchoolGroup attatchBase64ToGroup(SchoolGroup group) throws IOException {
        group.setBase64Encoded(transferToBase64(group.getPhoto()));

        return group;
    }

    public static Event attatchBase64ToEvent(Event event) throws IOException {
        event.setBase64Encoded(transferToBase64(event.getEventImage()));

        return event;
    }

    public static byte[] transferToBytes(MultipartFile image){
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
    
    public static String transferToBase64(byte[] photo) throws IOException {
        //In case, the object did not have image set
        if(photo==null){
            //Set default avatar
            File defaultAvatar=new File("src/main/resources/static/img/default_image.png");
            String absolutePath = defaultAvatar.getAbsolutePath();
            File a=new File(absolutePath);

            MultipartFile avatar=new MockMultipartFile("avatar", Files.readAllBytes(a.toPath()));

            byte[] encodeBase64 = Base64.getEncoder().encode(Files.readAllBytes(a.toPath()));

            return new String(encodeBase64, "UTF-8");
        }else{
            byte[] encodeBase64 = Base64.getEncoder().encode(photo);

            return new String(encodeBase64, "UTF-8");
        }
    }
}