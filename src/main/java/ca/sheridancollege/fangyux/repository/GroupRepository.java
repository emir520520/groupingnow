package ca.sheridancollege.fangyux.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.SchoolGroup;



@Repository
public interface GroupRepository extends JpaRepository<SchoolGroup, Long> {

	public List<SchoolGroup> findByOrderByIdAsc();
	
	public List<SchoolGroup> findByOrderByNameAsc();
	
	public List<SchoolGroup> findByOrderByCategoryAsc();
	
	public List<SchoolGroup> findByOrderByStudyAsc();
	
	public List<SchoolGroup> findByOrderByDescriptionAsc();
	
	@Query(value="SELECT * FROM school_group ORDER BY description DESC LIMIT 2",nativeQuery=true)
	List<SchoolGroup> getTwoGroups();

	@Query(value="SELECT * FROM school_group c WHERE c.user_id = ?1 ",nativeQuery = true)
	List<SchoolGroup> selectUserFromSchoolGroupByUserId(long userId);

	@Query(value="SELECT * FROM school_group WHERE admins like %:name% LIMIT 2",nativeQuery=true)
	List<SchoolGroup> getUserGroup(@Param("name")String name);

	@Query(value="SELECT group_id FROM cart_groups c WHERE c.user_id= ?1",nativeQuery=true)
	long getGroupId(long userId);

	@Query(value="SELECT user_id FROM school_group c WHERE c.id= ?1",nativeQuery=true)
	long getUserIdByGroupId(long groupId);

	@Query(value="SELECT name FROM school_group c WHERE c.id= ?1",nativeQuery=true)
	String getGroupNameByGroupId(long groupId);

	@Query(value="SELECT * FROM school_group WHERE admins like %:name% ORDER BY :#{#pageable}",
			countQuery = "SELECT count(*) FROM school_group WHERE admins like %:name%",
			nativeQuery=true)
	Page<SchoolGroup> getUserGroups(@Param("name")String name, Pageable pageable);

	@Query(value="SELECT * FROM school_group WHERE name LIKE %:name%",
			countQuery = "SELECT count(*) FROM school_group WHERE name LIKE %:name%",
			nativeQuery=true)
	Page<SchoolGroup> getGroupsByName(@Param("name")String name, Pageable pageable);
}