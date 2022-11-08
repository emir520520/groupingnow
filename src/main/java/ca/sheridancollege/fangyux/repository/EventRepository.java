package ca.sheridancollege.fangyux.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Event;

import javax.transaction.Transactional;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	@Query(value="SELECT event_id FROM cart_events c WHERE c.user_id= ?1",nativeQuery=true)
	long getEventId(long userId);

	@Query(value="SELECT * FROM event WHERE id=?1", nativeQuery = true)
	Event getEventById(Long id);

	@Query(value="SELECT * FROM event WHERE host_name like %:name% ORDER BY :#{#pageable}",
			countQuery = "SELECT count(*) FROM event WHERE host_name like %:name%",
			nativeQuery=true)
	Page<Event> getUserEvents(@Param("name")String name, Pageable pageable);

	@Query(value="SELECT * FROM event WHERE group_id=?1",
			countQuery = "SELECT count(*) FROM event WHERE group_id=?1",
			nativeQuery=true)
	Page<Event> getrEventsOfGroup(@Param("groupId")Long groupId, Pageable pageable);

	@Query(value="SELECT * FROM event WHERE id in :IDs",
			countQuery = "SELECT count(*) FROM event WHERE id in :IDs",
			nativeQuery=true)
	Page<Event> getEventsByIDs(@Param("IDs")List<Long> IDs, Pageable pageable);

	@Query(value="SELECT * FROM event WHERE event_name LIKE %:name%",
			countQuery = "SELECT count(*) FROM event WHERE event_name LIKE %:name%",
			nativeQuery=true)
	Page<Event> getEventsByName(@Param("name")String name, Pageable pageable);

	//---------------------------------Operation of cart_events table
	@Query(value="SELECT event_id FROM cart_events WHERE user_id=:userId",nativeQuery=true)
	List<Long> getEventsIDsByUserId(@Param("userId")Long userId);

	//---------------------------------Get the events will happen in the next two hours
	@Query(value = "SELECT id FROM event WHERE date = CURRENT_DATE() AND time > NOW() AND time < NOW()+INTERVAL 2 HOUR;", nativeQuery = true)
	List<Long> getEventsApproching();

	//---------------------------------Set the event remindered property to true
	@Modifying
	@Transactional
	@Query(value = "UPDATE event SET remindered='true' WHERE id=?1", nativeQuery = true)
	void setEventReminderedToTrue(Long id);

	//---------------------------------Get passed events IDs
	@Query(value="SELECT id FROM `event`WHERE date < CURRENT_DATE() OR (date=CURRENT_DATE AND time < NOW());", nativeQuery = true)
	List<Integer> getPassedEventsID();

	//----------------------------------Delete passed events
	@Modifying
	@Transactional
	@Query(value="DELETE FROM `event`WHERE date < CURRENT_DATE() OR (date=CURRENT_DATE AND time < NOW());", nativeQuery = true)
	void deletePassedEvents();
}