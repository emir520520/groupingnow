package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
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
import ca.sheridancollege.fangyux.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class HomeController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EventService eventService;

	@Autowired
	private GroupService groupService;

	@GetMapping("/")
	public String goHome(Model model, @AuthenticationPrincipal Authentication authentication) throws
			IOException {

		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		if(user!=null) {
			model.addAttribute("user", user.getFirstName());
		}

		//--------------------Top two events
		Page<Event> eventPage = eventService.getEventsPaginated(1, 3, "all");

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		model.addAttribute("events", events);

		//--------------------Top two groups
		Page<SchoolGroup> groupPage = groupService.getGroupsPaginated(1, 3, "all");

		List<SchoolGroup> groups = new ArrayList<>();

		groupPage.forEach(entity -> groups.add(entity));

		for (int i = 0; i < groups.size(); i++) {
			groups.set(i, ImageOperation.attatchBase64ToGroup(groups.get(i)));
		}

		model.addAttribute("groups", groups);

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
}