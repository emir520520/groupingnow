package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import ca.sheridancollege.fangyux.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {
	
	private EventRepository eventRepo;
	private GroupRepository groupRepo;
	private UserRepository userRepo;
	
	@GetMapping("/")
	public String goHome(Model model, @AuthenticationPrincipal Authentication authentication) throws
			IOException {
		
	//-------------------------------------------Authentication
	Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
	User user = userRepo.findByEmail(auth.getName());
	
	if(user!=null) {
		model.addAttribute("user",user.getFirstName());
		
		//--------------------Top two events
				List<Event> events=eventRepo.getUserEvents(user.getFirstName());
				
				for(int i=0;i<events.size();i++) {
					events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
				}

				model.addAttribute("events",events);
				
		//--------------------Top two groups
		List<SchoolGroup> groups=groupRepo.getUserGroup(user.getFirstName());
		
		for(int i=0;i<groups.size();i++) {
			groups.set(i, ImageOperation.attatchBase64ToGroup(groups.get(i)));
		}

		model.addAttribute("groups",groups);
	}else {
		//--------------------Top two events
		List<Event> events=eventRepo.getTwoEvents();
		
		for(int i=0;i<events.size();i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		model.addAttribute("events",events);
		
		//--------------------Top two groups
		List<SchoolGroup> groups=groupRepo.getTwoGroups();
		
		for(int i=0;i<groups.size();i++) {
			groups.set(i, ImageOperation.attatchBase64ToGroup(groups.get(i)));
		}

		model.addAttribute("groups",groups);
	}

	
	return "home.html";
}
}