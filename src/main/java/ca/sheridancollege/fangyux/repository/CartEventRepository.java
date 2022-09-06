package ca.sheridancollege.fangyux.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.CartEvent;
import ca.sheridancollege.fangyux.beans.User;

@Repository
public interface CartEventRepository extends JpaRepository<CartEvent, Long> {

	public List<CartEvent> findByUser(User user);
}
