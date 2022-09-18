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
    public void updateQuantity(Integer quantity, Integer userId, Integer eventId);

    @Modifying
    @Query("DELETE FROM CartEvent c WHERE c.user.id = ?1 AND c.event.id = ?2")
    public void deleteByUserAndEvent(User user, Event event);
}