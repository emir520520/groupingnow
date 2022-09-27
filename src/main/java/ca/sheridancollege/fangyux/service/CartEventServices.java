package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.CartEvent;
import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.CartEventRepository;
import ca.sheridancollege.fangyux.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartEventServices {
    @Autowired
    private CartEventRepository carteventrepository;
    @Autowired
    private EventRepository eventrepository;

    public List<CartEvent> listCartEvents(User user){
        return carteventrepository.findByUser(user);
    }

    public Integer addEvent(Long eventId, User user){
        Integer updatedQuantity = 3;
        Event event = eventrepository.getById(eventId);

        CartEvent cartEvent = carteventrepository.findByUserAndEvent(user, event);

        if(cartEvent != null) {
            updatedQuantity = cartEvent.getQuantity() + 3;
        } else{
            cartEvent = new CartEvent();
            cartEvent.setUser(user);
            cartEvent.setEvent(event);
        }

        cartEvent.setQuantity(updatedQuantity);
        carteventrepository.save(cartEvent);

        return updatedQuantity;

    }
}
