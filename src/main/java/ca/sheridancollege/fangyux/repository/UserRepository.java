package ca.sheridancollege.fangyux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.sheridancollege.fangyux.beans.User;


public interface UserRepository extends JpaRepository<User, Long>{

	@Query(value="SELECT * FROM user WHERE email LIKE %:email%",nativeQuery=true)
	User findByEmail(@Param("email")String email);
}