package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.*;
import ca.sheridancollege.fangyux.repository.CartEventRepository;
import ca.sheridancollege.fangyux.repository.CartGroupRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import ca.sheridancollege.fangyux.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartGroupServices {
    @Autowired
    private CartGroupRepository cartgrouprepository;
    @Autowired
    private GroupRepository grouprepository;

    public List<CartGroup> listCartGroups(User user){
        return cartgrouprepository.findByUser(user);
    }

    public Integer addGroup(Long groupId, User user){
        Integer updatedParticipants = 3;
        SchoolGroup group = grouprepository.getById(groupId);

        CartGroup cartGroup = cartgrouprepository.findByUserAndGroup(user, group);

        if(cartGroup != null) {
            updatedParticipants = cartGroup.getParticipants() + 3;
        } else{
            cartGroup = new CartGroup();
            cartGroup.setUser(user);
            cartGroup.setGroup(group);
        }

        cartGroup.setParticipants(updatedParticipants);
        cartgrouprepository.save(cartGroup);

        return updatedParticipants;

    }
}
