package ca.sheridancollege.fangyux.service;

import java.util.List;

import ca.sheridancollege.fangyux.beans.Event;
import org.springframework.data.domain.Page;


import org.springframework.stereotype.Service;

@Service
public interface EventService {

	List<Event> getAllEvents();

	void saveEvent(Event event);

	Event getEventById(long id);
	void deleteEventById(long id);
	Page<Event> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	long getEventIdByUserId(long userId);
  	Page<Event> getEventsPaginated(int pageNo, int pageSize, String scope);
  	void save(Event event);
    Page<Event> getrEventsOfGroup(int pageNo, int pageSize, Long groupId);
	Page<Event> getEventsByIDs(int PageNum, int pageSize, List<Long> eventIDs);

	Page<Event> getEventsByName(int PageNum, int pageSize, String name);

	List<Long> getEventIdByUserIdAndGroupId(long userId);
}