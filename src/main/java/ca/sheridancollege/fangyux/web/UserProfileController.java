package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.beans.Topic;
import ca.sheridancollege.fangyux.service.TopicService;
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

	@Autowired
	private TopicService topicService;

	private UserDetails userDetails;
	private User user;
	
	@GetMapping("userProfile")
	public String userProfile(Model model) throws IOException {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());

		user=ImageOperation.attatchBase64ToUser(user);
		
		model.addAttribute("originalUser", user);
		return ("userProfile");
	}
	
	@GetMapping("userProfileEdit")
	public String userProfileEdit(Model model) throws IOException {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());

		//transfer image type
		this.user=ImageOperation.attatchBase64ToUser(user);

		//get all topics
		ArrayList<Topic> topics= (ArrayList<Topic>) topicService.getAllTopics();

		//get topics that current user picked
		ArrayList<Topic> topicsOfUser= (ArrayList<Topic>) topicService.getTopicsOfUser(user.getId());



		model.addAttribute("topics",topics);
		model.addAttribute("originalUser", this.user);
		model.addAttribute("user", new User());
		return ("userProfileEdit");
	}

	@PostMapping("userProfileEdit")
	public String userProfileEditSubmit(User user, Model model,
			@RequestParam(name="image", required = false) MultipartFile multipartFile) throws IOException {

		if(multipartFile.isEmpty()){
			userService.updateUser(user);
		}else{
			//Attach image to the user
			user=ImageOperation.attatchToUser(user, multipartFile);

			userService.updateUserWithAvatar(user);
		}

		//Get updated user and prepare it for redirection
		User updatedUser=userService.getUserById(user.getId());

		updatedUser=ImageOperation.attatchBase64ToUser(updatedUser);

		model.addAttribute("originalUser", updatedUser);
		return ("userProfile");
	}
}