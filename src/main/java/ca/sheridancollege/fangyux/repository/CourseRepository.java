package ca.sheridancollege.fangyux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.sheridancollege.fangyux.beans.Course;


public interface CourseRepository extends JpaRepository<Course, Long> {
	
	@Query(value="SELECT * FROM course WHERE id=:id", nativeQuery=true)
	Course findCourseByID(@Param("id")Long id);
}