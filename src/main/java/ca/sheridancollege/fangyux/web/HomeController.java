package ca.sheridancollege.fangyux.web;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.service.EventService;
import ca.sheridancollege.fangyux.service.GroupService;
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
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class HomeController {

	@Autowired
	private EventService eventService;
	@Autowired
	private GroupService groupService;

	private EventRepository eventRepo;
	private GroupRepository groupRepo;
	private UserRepository userRepo;
	
	@GetMapping("/")
	public String goHome(Model model, @AuthenticationPrincipal Authentication authentication) throws 
	UnsupportedEncodingException {
		
	//-------------------------------------------Authentication
	Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
	User user = userRepo.findByEmail(auth.getName());
	
	if(user!=null) {
		model.addAttribute("user",user.getFirstName());
		
		//--------------------Top two events
				List<Event> events=eventRepo.getUserEvents(user.getFirstName());
				
				for(int i=0;i<events.size();i++) {
					byte[] encodeBase64 = Base64.getEncoder().encode(events.get(i).getEventImage());
					String base64Encoded = new String(encodeBase64, "UTF-8");
					events.get(i).setBase64Encoded(base64Encoded);
				}

				model.addAttribute("events",events);
				
		//--------------------Top two groups
		List<SchoolGroup> groups=groupRepo.getUserGroup(user.getFirstName());
		
		for(int i=0;i<groups.size();i++) {
			byte[] encodeBase64 = Base64.getEncoder().encode(groups.get(i).getPhoto());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			groups.get(i).setBase64Encoded(base64Encoded);
		}

		model.addAttribute("groups",groups);
	}else {
		//--------------------Top two events
		List<Event> events=eventRepo.getTwoEvents();
		
		for(int i=0;i<events.size();i++) {
			byte[] encodeBase64 = Base64.getEncoder().encode(events.get(i).getEventImage());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			events.get(i).setBase64Encoded(base64Encoded);
		}

		model.addAttribute("events",events);
		
		//--------------------Top two groups
		List<SchoolGroup> groups=groupRepo.getTwoGroups();
		
		for(int i=0;i<groups.size();i++) {
			byte[] encodeBase64 = Base64.getEncoder().encode(groups.get(i).getPhoto());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			groups.get(i).setBase64Encoded(base64Encoded);
		}

		model.addAttribute("groups",groups);
	}

	
	return "home.html";
}
	@GetMapping("/findDetailsEvent/{id}")
	public String goFindDetailEvent(@PathVariable (value = "id") Long id, Model model) throws
			UnsupportedEncodingException{


		Event event = eventService.getEventById(id);

		event= ImageOperation.attatchBase64ToEvent(event);
		//set event as a model
		model.addAttribute("events",event);
			return "findDetailsEvent.html";

	}
	@GetMapping("/findDetailsGroup/{id}")
	public String goFindDetailGroup(@PathVariable (value = "id") Long id, Model model) throws
			UnsupportedEncodingException{
		SchoolGroup group = groupService.getGroupById(id);
		group= ImageOperation.attatchBase64ToGroup(group);
		//set group as a model
		model.addAttribute("groups",group);
		return "findDetailsGroup.html";

	}
}