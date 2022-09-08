package ca.sheridancollege.fangyux.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.EventRepository;


@Service
public class EventServiceImpl implements EventService{
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<Event> getAllEvents(){
		return eventRepository.findAll();
	}

	@Override
	public void saveEvent(Event event) {
		this.eventRepository.save(event);
	}

	@Override
	public Event getEventById(long id) {
		Optional<Event> optional = eventRepository.findById(id);
		Event event = null;
		if(optional.isPresent()) {
			event = optional.get();
		}else {
			throw new RuntimeException("Event do not found for id: " + id);
		}
		return event;
	}

	@Override
	public void deleteEventById(long id) {
		// TODO Auto-generated method stub
		//this.carteventrepository.deleteById(id);
		this.eventRepository.deleteById(id);
	}

	@Override
	public Page<Event> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.eventRepository.findAll(pageable);
	}

	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}
}
