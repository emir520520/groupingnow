package ca.sheridancollege.fangyux.web;

import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import ca.sheridancollege.fangyux.service.EventService;
import ca.sheridancollege.fangyux.service.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;


@Controller
public class MainController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userrepo;
	
	@GetMapping("/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication == null || authentication instanceof AnonymousAuthenticationToken ) {
			return "login";
		}
		else
		{
			return "redirect:/";

		}
	}
} 