package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.service.CartEventServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.repository.UserRepository;
import ca.sheridancollege.fangyux.service.EventService;


@Controller
@AllArgsConstructor
public class EventController {

	@Autowired
	private CartEventServices cartEventServices;
	@Autowired
	private EventService eventService;
	private UserRepository userRepo;
	
	@GetMapping("/events")
	public String home(Model model) {
		return findPaginated(1,"eventName", "asc", model);
	}

	@GetMapping("/addEventToCart/{eventId}")
	public String addEventToCart(@PathVariable("eventId") Long eventId, @AuthenticationPrincipal Authentication authentication){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userRepo.findByEmail(auth.getName());
			Integer updatedQuantity = cartEventServices.addEvent(eventId, user);
			return "redirect:/events";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}
	//display list of event
	@GetMapping("/addEvent")
	public String showNewEventForm(Model model) {

		List<String> typeOfEventList = Arrays.asList("Online","In Person (Indoor)","In Person (Outdoor)");
		List<String> categoriesList = Arrays.asList("Food", "Music", "Health", "Fashion", "Business", "Sport", "Education", 
				"Art", "Party", "Entertainment", "Others");
		
		model.addAttribute("typeOfEventList",typeOfEventList);
		model.addAttribute("categoriesList",categoriesList);
		
		Event event = new Event();
		model.addAttribute("event", event);
		return "newEvent.html";
	}
	 
	@PostMapping("/addEvent")
    public String addEvent(@ModelAttribute("event") Event event, @RequestParam(value = "image", required = true)MultipartFile file, @AuthenticationPrincipal Authentication authentication){
//		String id = String.valueOf(UUID.randomUUID());
	    Blob blob = null;
	    byte[] blobAsBytes=null;
	    try {
	        blob = new SerialBlob(file.getBytes());
	        
	        int blobLength = (int) blob.length();  
	        blobAsBytes = blob.getBytes(1, blobLength);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    event.setEventImage(blobAsBytes);
	    
	    System.out.println(event.getEventImage());
	    eventService.save(event);
	    
	    return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
		//get event from service
		Event event = eventService.getEventById(id);
		
		//set event as a model
		model.addAttribute("event",event);
		return "editEvent";
	}
	
	@GetMapping("/deleteEvent/{id}")
	public String deleteEvent(@PathVariable (value="id") long id) {
		//delete event from service
		//this.cartEventServices.
		this.eventService.deleteEventById(id);
		return "redirect:/";
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value="pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir , Model model) {
		int pageSize = 5;

		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		if(user!=null) {
			model.addAttribute("user", user.getFirstName());
		}
	
		Page<Event> page = eventService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Event> listEvents = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listEvents", listEvents);
		
		return "viewEvents.html";
	}

	@RequestMapping("/myEvent/paginated")
	@ResponseBody
	public ResultEntity<List<Event>> getEventPaginated(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam(value = "keyword", defaultValue = "")String keyword
	) throws IOException {
		Page<Event> eventPage = eventService.getEventsPaginated(pageNum, pageSize, "all");

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}
}
