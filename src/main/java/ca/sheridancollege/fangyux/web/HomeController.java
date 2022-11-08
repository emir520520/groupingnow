package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
import ca.sheridancollege.fangyux.repository.*;
import ca.sheridancollege.fangyux.service.EventService;
import ca.sheridancollege.fangyux.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class HomeController {

	@Autowired
	private EventService eventService;
	@Autowired
	private GroupService groupService;

	@Autowired
	private CartEventRepository cartEventRepo;

	@Autowired
	private CartGroupRepository cartGroupRepo;
	private UserRepository userRepo;


	@GetMapping("/")
	public String goHome(Model model, @AuthenticationPrincipal Authentication authentication) throws
			IOException {

		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		System.out.println("User : " + auth.getName());
		User user = userRepo.findByEmail(auth.getName());
		//System.out.println("User email controller: " + user.getEmail());
		System.out.println("auth principal: " + auth.getPrincipal());
		if(user!=null) {
			model.addAttribute("user", user.getFirstName());
		}
		else {
			System.out.println("Fail to load user");
		}

		return "home.html";
	}

	@RequestMapping("/event/paginated")
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
  
	@GetMapping("/findDetailsEvent/{groupId}/{eventId}")
	public String goFindDetailEvent(@PathVariable ("groupId") Long groupId,@PathVariable (value = "eventId") Long eventId, Model model) throws
			IOException {
		//-------------------------------------------Authentication for verify to prevent user clicks pick btn
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		if(user!=null) {
			model.addAttribute("user", user.getFirstName());

			//--------------------------------------------If the current event is picked already, display some msg in the front-end
			int result=cartEventRepo.checkCartEventOfUser(eventId, user.getId());

			if(result==1){
				model.addAttribute("cart","picked");
			}else{
				model.addAttribute("cart","not");
			}
		}

		Event event = eventService.getEventById(eventId);
		SchoolGroup group = groupService.getGroupById(groupId);

		System.out.println(group.getId());

		event= ImageOperation.attatchBase64ToEvent(event);
		//set event as a model
		model.addAttribute("events",event);
		model.addAttribute("groups",group);
			return "findDetailsEvent.html";

	}
  
	@GetMapping("/findDetailsGroup/{id}")
	public String goFindDetailGroup(@PathVariable (value = "id") Long id, Model model) throws
			IOException {
		//-------------------------------------------Authentication for verify to prevent user clicks pick btn
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		if(user!=null) {
			model.addAttribute("user", user.getFirstName());

			//--------------------------------------------If the current event is picked already, display some msg in the front-end
			int result=cartGroupRepo.checkCartGroupOfUser(id, user.getId());

			if(result==1){
				model.addAttribute("cart","picked");
			}else{
				model.addAttribute("cart","not");
			}
		}

		SchoolGroup group = groupService.getGroupById(id);
		group= ImageOperation.attatchBase64ToGroup(group);
		//set group as a model
		model.addAttribute("groups",group);
		return "findDetailsGroup.html";
  }

	@RequestMapping("/group/paginated")
	@ResponseBody
	public ResultEntity<List<SchoolGroup>> getGroupPaginated(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam(value = "keyword", defaultValue = "")String keyword
	) throws IOException {
		Page<SchoolGroup> groupPage = groupService.getGroupsPaginated(pageNum, pageSize, "all");

		List<SchoolGroup> groups = new ArrayList<>();

		groupPage.forEach(entity -> groups.add(entity));

		for (int i = 0; i < groups.size(); i++) {
			groups.set(i, ImageOperation.attatchBase64ToGroup(groups.get(i)));
		}

		Long totalRecords=groupPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(groups, totalRecords);
	}

	@GetMapping("/search")
	public String gotoSearchEventsPage(
			@RequestParam("category")String category,
			@RequestParam("name")String name,
			Model model){
		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		if(user!=null){
			model.addAttribute("user", user.getFirstName());
		}

		if(category.equals("Event")){
			model.addAttribute("eventName", name);

			return "searchEvents.html";
		}else{
			model.addAttribute("groupName", name);

			return "searchGroups.html";
		}
	}
}