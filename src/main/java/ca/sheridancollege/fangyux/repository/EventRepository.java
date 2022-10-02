package ca.sheridancollege.fangyux.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	@Query(value="SELECT event_id FROM cart_events c WHERE c.user_id= ?1",nativeQuery=true)
	long getEventId(long userId);

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
}