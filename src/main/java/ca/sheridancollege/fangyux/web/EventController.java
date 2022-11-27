package ca.sheridancollege.fangyux.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.Utils.ResultEntity;
import ca.sheridancollege.fangyux.beans.Announcement;
import ca.sheridancollege.fangyux.beans.SchoolGroup;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.*;
import ca.sheridancollege.fangyux.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.fangyux.beans.Event;


@Controller
@AllArgsConstructor
public class EventController {

	@Autowired
	private CartEventServices cartEventServices;
	@Autowired
	private CartEventGroupServices cartEventGroupServices;
	@Autowired
	private EventService eventService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	private UserRepository userRepo;

	@Autowired
	private EventRepository eventRepo;
	@Autowired
	private GroupRepository groupRepo;
	@Autowired
	private AnnouncementRepository announcementRepository;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private CartGroupRepository cartGroupRepo;

	@Autowired
	private CartGroupEventRepository cartGroupEventRepo;
	
	@GetMapping("/events")
	public String home(Model model) {
		//-------------------------------------------Authentication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		Long userId=user.getId();
		List<Long> groupIdList=cartGroupEventRepo.getGroupIdsByUserId(userId);
		List<SchoolGroup> groupList = new ArrayList<>();

		for(int i=0;i<groupIdList.size();i++)
		{
			groupList.add(groupRepo.getById(groupIdList.get(i)));
		}

		model.addAttribute("user", user.getFirstName());
		model.addAttribute("groups", groupList);


		return findPaginated(userId,model);
	}
	@GetMapping("/addGroupEventToCart/{groupId}/{eventId}")
	public String addGroupEventToCart(@PathVariable("groupId") Long groupId,@PathVariable("eventId") Long eventId, @AuthenticationPrincipal Authentication authentication) throws AddressException, MessagingException, IOException{
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//get event information
			Event event = eventService.getEventById(eventId);
			SchoolGroup group = groupService.getGroupById(groupId);
			String content;
			content =
					"<h3>You have joined successfully event !!!</h3>" +
							"<br>Event name: " + event.getEventName() +
							"<br>Description: " + event.getDescription() +
							"<br>Host name: " + event.getHostName() +
							"<br>Type of Event: " + event.getTypeOfEvent() +
							"<br>Category: " + event.getCategory() +
							"<br>Location: " + event.getLocation() +
							"<br>Date: " +event.getDate() +
							"<br>Time: " + event.getTime();
			//send email to user with event information
			sendmail(auth.getName(), content);

			User user = userRepo.findByEmail(auth.getName());
			Integer updatedQuantity = cartEventGroupServices.addGroupEvent(groupId, eventId, user);
			cartEventServices.addEvent(eventId, user);

			return "redirect:/events";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}
	@GetMapping("/addEventToCart/{eventId}")
	public String addEventToCart(@PathVariable("eventId") Long eventId, @AuthenticationPrincipal Authentication authentication) throws AddressException, MessagingException, IOException{
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//get event information
			Event event = eventService.getEventById(eventId);
			String content;
			content =
					"<h3>You have joined successfully event !!!</h3>" +
					"<br>Event name: " + event.getEventName() +
					"<br>Description: " + event.getDescription() +
					"<br>Host name: " + event.getHostName() +
					"<br>Type of Event: " + event.getTypeOfEvent() +
					"<br>Category: " + event.getCategory() +
					"<br>Location: " + event.getLocation() +
					"<br>Date: " +event.getDate() +
					"<br>Time: " + event.getTime();
			//send email to user with event information
			sendmail(auth.getName(), content);

			User user = userRepo.findByEmail(auth.getName());
			Integer updatedQuantity = cartEventServices.addEvent(eventId, user);

			eventRepo.increaseNumOfAttendance(eventId);

			return "redirect:/events";
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}

	@GetMapping("/inviteGroupMember/{eventId}/{userId}")
	public String inviteGroupMember(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId, @AuthenticationPrincipal Authentication authentication, HttpServletRequest request) throws AddressException, MessagingException, IOException{
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			//get event information
			Event event = eventService.getEventById(eventId);
			SchoolGroup group = groupService.getGroupById(event.getGroupId());
			String content;
			content ="Dear [[name]],<br>"+
					"<h3>You got invite to this event from group '" + group.getName() +"'</h3>"+
							"<br>Event name: " + event.getEventName() +
							"<br>Description: " + event.getDescription() +
							"<br>Host name: " + event.getHostName() +
							"<br>Type of Event: " + event.getTypeOfEvent() +
							"<br>Category: " + event.getCategory() +
							"<br>Location: " + event.getLocation() +
							"<br>Date: " +event.getDate() +
							"<br>Time: " + event.getTime()
					+ "<br><br>Please click the link below to verify your registration:<br>"
					+ "<h3><a href=\"[[URL]]\" target=\"_self\">Join</a></h3>"
					+ "Thank you,<br>"
					+ "Grouping Now team.";

			//send email to user with event information
			User user = userRepo.getUserByUserId(userId);
			User receiver = userRepo.findByEmail(auth.getName());

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(auth.getName(), receiver.getFirstName() + " " + receiver.getLastName());
			helper.setTo(user.getEmail());
			helper.setSubject("Get Invite f");
			content = content.replace("[[name]]", user.getFirstName() + user.getLastName());


			String siteURL = request.getRequestURL().toString();
			String hostURL=siteURL.replace(request.getServletPath(), "");
			String xURL =hostURL+"/acceptInviteToEvnt/"+event.getGroupId()+"/"+eventId+"?userId="+userId;
			content = content.replace("[[URL]]", xURL);

			sendmail(user.getEmail(), content);


			return "redirect:/goTrackMyEvents/" + event.getGroupId() + "/" + eventId;
		} catch(UsernameNotFoundException ex){
			System.out.println("You must login to add this event to cart");
			return "You must login to add this event to cart";
		}
	}

	@GetMapping("/acceptInviteToEvnt/{groupId}/{eventId}")
	public String verifyUser(@PathVariable("groupId") Long groupId, @PathVariable("eventId") Long eventId, @Param("userId") String userId) throws MessagingException, UnsupportedEncodingException {
		String result = cartEventGroupServices.acceptInviteToEvent(groupId, eventId, Long.valueOf(userId));

		if (result.equals("success")) {
			return "accept_join_event_success";
		} else {
			return "accept_join_event_fail";
		}
	}

	//display list of event
	@GetMapping("/addEvent/{groupId}")
	public String showNewEventForm(Model model, @PathVariable("groupId")Long groupId) {

		List<String> typeOfEventList = Arrays.asList("Online","In Person (Indoor)","In Person (Outdoor)");
		List<String> categoriesList = Arrays.asList("Food", "Music", "Health", "Fashion", "Business", "Sport", "Education",
				"Art", "Party", "Entertainment", "Others");

		model.addAttribute("typeOfEventList",typeOfEventList);
		model.addAttribute("categoriesList",categoriesList);
		model.addAttribute("groupId",groupId);

		Event event = new Event();
		model.addAttribute("event", event);
		return "newEvent.html";
	}

	@PostMapping("/addEvent/{groupId}")
	public String addEvent(@PathVariable (value = "groupId") String groupId, @ModelAttribute("event") Event event, @RequestParam(value = "image", required = true)MultipartFile file, @AuthenticationPrincipal Authentication authentication) throws MessagingException, IOException {
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

		Long groupIdLong=Long.parseLong(groupId);
		event.setGroupId(groupIdLong);
		event.setRemindered("false");
		eventRepo.save(event);

		String content;
		content =
				"<h3>New Event just created in Group: " + groupRepo.getGroupNameByGroupId(groupIdLong) +
				"</h3><br>Event name: " + event.getEventName() +
				"<br>Description: " + event.getDescription() +
				"<br>Host name: " + event.getHostName() +
				"<br>Type of Event: " + event.getTypeOfEvent() +
				"<br>Category: " + event.getCategory() +
				"<br>Location: " + event.getLocation() +
				"<br>Date: " +event.getDate() +
				"<br>Time: " + event.getTime();
		;
		//send email to user with event information
		for(int i = 0; i < cartGroupRepo.selectAllUserIdByGroupId(groupIdLong).size();i++)
		{
			sendmail(userRepo.getUserEmailByUserId(cartGroupRepo.selectAllUserIdByGroupId(groupIdLong).get(i)), content);
		}


		return "viewGroups.html";
	}

	@GetMapping("/addAnnouncement/{groupId}/{eventId}")
	public String loadAddAnnouncement(Model model, @PathVariable("groupId")Long groupId, @PathVariable("eventId")Long eventId) {
		model.addAttribute("announcementpost", new Announcement());
		model.addAttribute("users", userRepo.findAll());
		model.addAttribute("groupId",groupId);
		model.addAttribute("eventId",eventId);
		return "redirect:/";
	}

	@PostMapping("/addAnnouncement/{groupId}/{eventId}")
	public String addAnnouncement(@PathVariable("groupId")Long groupId, @PathVariable("eventId")Long eventId,@ModelAttribute("announcementpost") Announcement announcement, Model model, @AuthenticationPrincipal Authentication authentication) throws IOException{
		Long hostId = groupRepo.getUserIdByGroupId(groupId);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		//get host name
		User hostUser = userService.getUserById(hostId);
		String hostName = hostUser.getFirstName() + " " + hostUser.getLastName();

		announcement.setEventId(eventId);
		announcement.setGroupId(groupId);
		announcement.setUserId(hostId);
		announcement.setHostName(hostName);
		announcementRepository.save(announcement);
		return "redirect:/goTrackMyEvents/" + groupId + "/" + eventId;
	}

	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable (value = "id") long id, Model model) {
		//get event from service
		Event event = eventService.getEventById(id);
		
		//set event as a model
		model.addAttribute("event",event);
		return "editEvent";
	}
	@GetMapping("/goTrackEvents/{groupId}/{eventId}")
	public String goTrackEvent(@PathVariable (value = "groupId") Long groupId,@PathVariable (value = "eventId") Long eventId, Model model) throws
			IOException {
		Long hostId = groupRepo.getUserIdByGroupId(groupId);
		User host = userService.getUserById(hostId);
		host = ImageOperation.attatchBase64ToUser(host);

		Event event = eventService.getEventById(eventId);
		SchoolGroup group = groupService.getGroupById(groupId);

		List<User> users = new ArrayList<>();
		List<User> groupMember = new ArrayList<>();
		List<User> groupMemberLeft = new ArrayList<>();
		event= ImageOperation.attatchBase64ToEvent(event);
		group= ImageOperation.attatchBase64ToGroup(group);

		event.setNumOfAttentdance(cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).size());

		for(int i = 0; i < cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			users.add(user);
		}
		for(int i = 0; i < cartGroupRepo.selectAllUserIdByGroupId(groupId).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartGroupRepo.selectAllUserIdByGroupId(groupId).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			groupMember.add(user);
		}
		for (int i = 0; i < groupMember.size(); i++)
		{
			int j;

			for (j = 0; j < users.size(); j++)
				if (groupMember.get(i).getId() == users.get(j).getId())
					break;

			if (j == users.size())
			{
				System.out.print(groupMember.get(i).getId() + " ");
				groupMemberLeft.add(groupMember.get(i));
			}
		}
		//test
		System.out.println("userID: " + hostId);
		System.out.println("groupId: " + groupId);
		System.out.println("eventId: " + eventId);

		//get announcement
		List<Announcement> announcements = new ArrayList<>();
		announcements = announcementRepository.getAnnouncementsByUserIdAndGroupIdAndEventId(hostId, groupId, eventId);

		//set group as a model
		model.addAttribute("groups",groupMemberLeft);
		model.addAttribute("events",event);
		model.addAttribute("users",users);
		model.addAttribute("hosts",host);
		model.addAttribute("announcements",announcements);
		return "trackEvents.html";
	}

	@GetMapping("/goTrackMyEvents/{groupId}/{eventId}")
	public String goTrackMyEvent(@PathVariable (value = "groupId") Long groupId,@PathVariable (value = "eventId") Long eventId, Model model) throws
			IOException {
		Long hostId = groupRepo.getUserIdByGroupId(groupId);
		User host = userService.getUserById(hostId);
		host = ImageOperation.attatchBase64ToUser(host);

		Event event = eventService.getEventById(eventId);
		SchoolGroup group = groupService.getGroupById(groupId);

		List<User> users = new ArrayList<>();
		List<User> groupMember = new ArrayList<>();
		List<User> groupMemberLeft = new ArrayList<>();
		event= ImageOperation.attatchBase64ToEvent(event);
		group= ImageOperation.attatchBase64ToGroup(group);

		event.setNumOfAttentdance(cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).size());

		for(int i = 0; i < cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartGroupEventRepo.getAllUserIdByGroupIdAndEventId(groupId, eventId).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			users.add(user);
		}
		for(int i = 0; i < cartGroupRepo.selectAllUserIdByGroupId(groupId).size();i++)
		{
			User user = new User();
			user = userRepo.getUserByUserId(cartGroupRepo.selectAllUserIdByGroupId(groupId).get(i));
			user=ImageOperation.attatchBase64ToUser(user);
			groupMember.add(user);
		}
		for (int i = 0; i < groupMember.size(); i++)
		{
			int j;

			for (j = 0; j < users.size(); j++)
				if (groupMember.get(i).getId() == users.get(j).getId())
					break;

			if (j == users.size())
			{
				System.out.print(groupMember.get(i).getId() + " ");
				groupMemberLeft.add(groupMember.get(i));
			}
		}
		//test
		//System.out.println("userID: " + hostId);
		//System.out.println("groupId: " + groupId);
		//System.out.println("eventId: " + eventId);

		//get announcement
		List<Announcement> announcements = new ArrayList<>();
		announcements = announcementRepository.getAnnouncementsByUserIdAndGroupIdAndEventId(hostId, groupId, eventId);

		for(int i=0; i< announcements.size();i++){
			System.out.println(announcements.get(i).getAnnouncement());
		}
		//set group as a model
		model.addAttribute("groups",groupMemberLeft);
		model.addAttribute("events",event);
		model.addAttribute("users",users);
		model.addAttribute("hosts",host);
		model.addAttribute("announcements",announcements);
		model.addAttribute("announcementpost", new Announcement());
		return "trackMyEvents.html";
	}
	
	@GetMapping("/deleteEvent/{id}")
	public String deleteEvent(@PathVariable (value="id") long id) {
		//delete event from service
		//this.cartEventServices.
		this.eventService.deleteEventById(id);
		return "redirect:/";
	}
	@GetMapping("/leaveEvent/{groupId}/{eventId}")
	public String leaveEvent(@PathVariable (value = "groupId") long groupId,@PathVariable (value = "eventId") long eventId, Model model,
							 @AuthenticationPrincipal Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepo.findByEmail(auth.getName());

		cartGroupEventRepo.deleteAllByUserIdAndGroupIdAndEventId(user.getId(), groupId, eventId);
		return "redirect:/events";
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(Long userId, Model model) {
		model.addAttribute("userId",userId);
		
		return "viewEvents.html";
	}

	@RequestMapping("/myEvent/paginated")
	@ResponseBody
	public ResultEntity<List<Event>> getEventPaginated(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam("userId")String uId//,
			//@RequestParam("groupId")String gId
	) throws IOException {
		//Get userId
		Long userId=Long.parseLong(uId);
		//Long groupId=Long.parseLong(gId);

		//Get Events IDs by userId from cart_events table
		//List<Long> eventIDs=eventRepo.getEventsIDsByUserId(userId);
		List<Long> eventIDs=eventRepo.getEventIdByUserIdAndGroupId(userId);
		//List<Long> groupIDs=cartEventGroupServices.getEventIdByUserIdAndEventId(uId,)
		Page<Event> eventPage = eventService.getEventsByIDs(pageNum, pageSize, eventIDs);

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}

	@PostMapping("/searchEvents")
	@ResponseBody
	public ResultEntity<List<Event>> getEventsByName(
			@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
			@RequestParam(value = "pageSize", defaultValue = "3")Integer pageSize,
			@RequestParam("eventName")String eventName
	) throws IOException {

		Page<Event> eventPage = eventService.getEventsByName(pageNum, pageSize, eventName);

		List<Event> events = new ArrayList<>();

		eventPage.forEach(entity -> events.add(entity));

		for (int i = 0; i < events.size(); i++) {
			events.set(i, ImageOperation.attatchBase64ToEvent(events.get(i)));
		}

		Long totalRecords=eventPage.getTotalElements();

		return ResultEntity.successWithtDataAndTotalRecoreds(events, totalRecords);
	}

	private void sendmail(String email, String content) throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("applicationgroupnow@gmail.com", "xzmaodwcvgqoctpt");
			}
		});

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("applicationgroupnow@gmail.com", false));

		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		msg.setSubject("Event information email");
		msg.setContent(content, "text/html");
		msg.setSentDate(new Date());

//		MimeBodyPart messageBodyPart = new MimeBodyPart();
//		messageBodyPart.setContent(content, "text/html");
//
//		Multipart multipart = new MimeMultipart();
//		multipart.addBodyPart(messageBodyPart);
//		MimeBodyPart attachPart = new MimeBodyPart();

		Transport.send(msg);
	}
}
