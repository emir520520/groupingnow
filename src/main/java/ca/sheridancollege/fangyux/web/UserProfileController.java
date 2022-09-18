package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.beans.CartEvent;
import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.Topic;
import ca.sheridancollege.fangyux.service.CartEventServices;
import ca.sheridancollege.fangyux.service.EventService;
import ca.sheridancollege.fangyux.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserProfileController {
	@Autowired
	private EventService eventService;
	@Autowired
	private CartEventServices carteventservices;
	@Autowired
	private UserService userService;

	@Autowired
	private TopicService topicService;

	private UserDetails userDetails;
	private User user;
	
	@GetMapping("userProfile")
	public String userProfile(Model model,@AuthenticationPrincipal Authentication authentication) throws UnsupportedEncodingException {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());

		user=ImageOperation.attatchBase64ToUser(user);
		
		model.addAttribute("originalUser", user);

		ModelAndView eventCart = new ModelAndView("eventCart");
		ModelAndView index = new ModelAndView("index");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByEmail(auth.getName());

		if (user == null)
		{
			return "home";
		}
		List<CartEvent> cartEvents = carteventservices.listCartEvents(user);
		/*long eventId = eventService.getEventIdByUserId(user.getId());
		System.out.println("Event ID: " + eventId);
		Event event = eventService.getEventById(eventId);
		event= ImageOperation.attatchBase64ToEvent(event);

		model.addAttribute("events",event);*/
		model.addAttribute("user",user);
		model.addAttribute("cartEvents",cartEvents);
		model.addAttribute("pageTitle","Event Cart");

		return ("userProfile");
	}
	
	@GetMapping("userProfileEdit")
	public String userProfileEdit(Model model) throws IOException {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());

		//transfer image type
		this.user=ImageOperation.attatchBase64ToUser(user);

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