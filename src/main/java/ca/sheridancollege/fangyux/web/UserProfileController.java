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
		
		byte[] encodeBase64 = Base64.getEncoder().encode(user.getPhoto());
		String base64Encoded = new String(encodeBase64, "UTF-8");
		user.setBase64Encoded(base64Encoded);
		
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
	public String userProfileEdit(Model model) {
		this.userDetails =
				 (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.user = userService.getUserByEmail(userDetails.getUsername());

		ArrayList<Topic> topics= (ArrayList<Topic>) topicService.getAllTopics();

		model.addAttribute("topics",topics);
		model.addAttribute("originalUser", this.user);
		model.addAttribute("user", new User());
		return ("userProfileEdit");
	}

	@PostMapping("userProfileEdit")
	public String userProfileEditSubmit(@ModelAttribute User user, Model model,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
//			User originalUser = userService.getUserByEmail(userDetails.getUsername());
//			if(user.getFirstName() != "") {
//				originalUser.setFirstName(user.getFirstName());
//			}
//			if(user.getLastName() != "") {
//				originalUser.setLastName(user.getLastName());
//			}
//			if(user.getProgram() != "") {
//				originalUser.setProgram(user.getProgram());
//			}
//			if(multipartFile != null ) {
//				Blob blob = null;
//			    byte[] blobAsBytes=null;
//			    try {
//			        blob = new SerialBlob(multipartFile.getBytes());
//
//			        int blobLength = (int) blob.length();
//			        blobAsBytes = blob.getBytes(1, blobLength);
//			        originalUser.setPhoto(blobAsBytes);
//			    } catch (Exception e) {
//			        e.printStackTrace();
//			    }
//			}
		//user= ImageOperation.attatchToUser(multipartFile);

			
		userService.saveUser(user);





		//model.addAttribute("originalUser", originalUser);
		return ("userProfile");
	}

}