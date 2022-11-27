package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.*;
import ca.sheridancollege.fangyux.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartEventGroupServices {
    @Autowired
    private CartGroupEventRepository cartGroupEventRepository;
    @Autowired
    private EventRepository eventrepository;
    @Autowired
    private GroupRepository grouprepository;

    @Autowired
    private UserRepository userRepo;

    public List<Long> getEventIdByUserIdAndEventId(Long userId,Long eventId){
        return cartGroupEventRepository.getGroupIdByUserIdAndEventId(userId,eventId);
    }
    public Integer addGroupEvent(Long groupId, Long eventId, User user){

        Event event = eventrepository.getById(eventId);
        SchoolGroup group = grouprepository.getById(groupId);

        CartEventGroup cartEventGroup = cartGroupEventRepository.findByUserAndEventAndGroup(user, event, group);

        if(cartEventGroup != null) {
            System.out.println("No cart event group");
        } else{
            cartEventGroup = new CartEventGroup();
            cartEventGroup.setUser(user.getId());
            cartEventGroup.setEvent(event.getId());
            cartEventGroup.setGroup(group.getId());
        }

        cartGroupEventRepository.save(cartEventGroup);

        return 0;

    }

    public String acceptInviteToEvent(Long groupId, Long eventId, Long userId){
        SchoolGroup group=grouprepository.getById(groupId);
        Event event=eventrepository.getById(eventId);
        User user=userRepo.getUserByUserId(userId);

        CartEventGroup ceg=new CartEventGroup();
        ceg.setGroup(group.getId());
        ceg.setEvent(event.getId());
        ceg.setUser(user.getId());

        try{
            cartGroupEventRepository.save(ceg);

            return "success";
        }catch (Exception e){
            return "fail";
        }
    }
}