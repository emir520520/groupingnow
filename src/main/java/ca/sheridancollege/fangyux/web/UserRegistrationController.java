package ca.sheridancollege.fangyux.web;

import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.fangyux.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Controller
@AllArgsConstructor
//@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	/*public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}*/
	
	/*@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }*/
	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	/*@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) throws IOException {
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}*/

	@PostMapping("/process_register")
	public String processRegister(Model model, User user, HttpServletRequest request)
			throws IOException, MessagingException {
		userService.register(user, getSiteURL(request));
		model.addAttribute("pageTitle","Registration Succeeded!");
		return "register/register_success";
	}

	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code, Model model, User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
		boolean verified = userService.verify(code);
		System.out.println("\n"+verified);
		System.out.println("\n"+code);
		if (verified == true) {
			return "register/verify_success";
		} else {
			return "register/verify_fail";
		}
	}
} 