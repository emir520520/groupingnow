package ca.sheridancollege.fangyux.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.beans.Event;
import ca.sheridancollege.fangyux.repository.EventRepository;


@Service
public class EventServiceImpl implements EventService{

	@Autowired
	private EventRepository eventRepository;

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
	public Page<Event> getEventsPaginated(int pageNo, int pageSize, String scope) {

		if(scope=="all"){
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
			return this.eventRepository.findAll(pageable);
		}else{
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
			return this.eventRepository.getUserEvents(scope, pageable);
		}
	}

	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}

	@Override
	public long getEventIdByUserId(long userId) {
		return this.eventRepository.getEventId(userId);
	}
}
