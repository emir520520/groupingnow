package ca.sheridancollege.fangyux.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
	
	@Query(value="SELECT * FROM event ORDER BY num_of_attendance LIMIT 2",nativeQuery=true)
	List<Event> getTwoEvents();
	
	@Query(value="SELECT * FROM event WHERE host_name like %:name% ORDER BY :#{#pageable}",
			countQuery = "SELECT count(*) FROM event WHERE host_name like %:name%",
			nativeQuery=true)
	Page<Event> getUserEvents(@Param("name")String name, Pageable pageable);
}