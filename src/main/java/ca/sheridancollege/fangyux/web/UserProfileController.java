package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.beans.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.service.UserService;


@Controller
public class UserProfileController {
	
	@Autowired
	private UserService userService;
	private UserDetails userDetails;
	private User user;
	
	@GetMapping("userProfile")
	public String userProfile(Model model) throws UnsupportedEncodingException {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());
		
		byte[] encodeBase64 = Base64.getEncoder().encode(user.getPhoto());
		String base64Encoded = new String(encodeBase64, "UTF-8");
		user.setBase64Encoded(base64Encoded);
		
		model.addAttribute("originalUser", user);
		return ("userProfile");
	}
	
	@GetMapping("userProfileEdit")
	public String userProfileEdit(Model model) {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());
		Topic defaultTopic = new Topic();
		model.addAttribute("originalUser", this.user);
		model.addAttribute("user", new User());
		return ("userProfileEdit");
	}

	@PostMapping("userProfileEdit")
	public String userProfileEditSubmit(@ModelAttribute User user, Model model,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
			User originalUser = userService.getUserByEmail(userDetails.getUsername());
			if(user.getFirstName() != "") {
				originalUser.setFirstName(user.getFirstName());
			}
			if(user.getLastName() != "") {
				originalUser.setLastName(user.getLastName());
			}
			if(user.getProgram() != "") {
				originalUser.setProgram(user.getProgram());
			}
			if(multipartFile != null ) {
				Blob blob = null;
			    byte[] blobAsBytes=null;
			    try {
			        blob = new SerialBlob(multipartFile.getBytes());
			        
			        int blobLength = (int) blob.length();  
			        blobAsBytes = blob.getBytes(1, blobLength);
			        originalUser.setPhoto(blobAsBytes);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
			
			userService.saveUser(originalUser);
			
			byte[] encodeBase64 = Base64.getEncoder().encode(originalUser.getPhoto());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			originalUser.setBase64Encoded(base64Encoded);
			
			model.addAttribute("originalUser", originalUser);
			return ("userProfile");
	}
}