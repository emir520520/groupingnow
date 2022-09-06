package ca.sheridancollege.fangyux.service;

import java.util.List;

import org.springframework.data.domain.Page;

import ca.sheridancollege.fangyux.beans.Event;

public interface EventService {

	List<Event> getAllEvents();
	void saveEvent(Event event);
	Event getEventById(long id);
	void deleteEventById(long id);
	void addEventToCart(long id);
	Page<Event> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	void save(Event event);
}
