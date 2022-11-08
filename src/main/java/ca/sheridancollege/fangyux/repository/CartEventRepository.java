package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.CartEvent;
import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartEventRepository extends JpaRepository<CartEvent, Long> {
    public List<CartEvent> findByUser(User user);


    public CartEvent findByUserAndEvent(User user, Event event);

    @Modifying
    @Query("Update CartEvent c SET c.quantity = ?1 WHERE c.user.id = ?2 AND c.event.id = ?3")
    void updateQuantity(Integer quantity, Integer userId, Integer eventId);

    @Modifying
    @Query("DELETE FROM CartEvent c WHERE c.user.id = ?1 AND c.event.id = ?2")
    void deleteByUserAndEvent(User user, Event event);

    @Query(value ="SELECT COUNT(*) FROM cart_events WHERE event_id=?1 AND user_id=?2", nativeQuery = true)
    int checkCartEventOfUser(Long eventId, Long userId);

    //---------------------------------Get the users that registered in the event
    @Query(value="SELECT user_id FROM cart_events WHERE event_id=?1", nativeQuery = true)
    List<Long> getUsersByEventID(Long eventID);
}
