package ca.sheridancollege.fangyux.service;

import java.util.List;

import ca.sheridancollege.fangyux.beans.Course;

public interface CourseService {

	List<Course> findAllCourses();
	
	void saveCourse(Course course, Long id);
	
	void deleteCourse(Long id);
	
	Course getCourseById(Long id);
}
