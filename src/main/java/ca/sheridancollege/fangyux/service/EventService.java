package ca.sheridancollege.fangyux.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import ca.sheridancollege.fangyux.beans.Event;

public interface EventService {

	List<Event> getAllEvents();
	void saveEvent(Event event);
	Event getEventById(long id);
	void deleteEventById(long id);
	Page<Event> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    Page<Event> getEventsPaginated(int pageNo, int pageSize, String scope);

    void save(Event event);
}
