package ca.sheridancollege.fangyux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.sheridancollege.fangyux.beans.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	/*@Query(value="SELECT * FROM user WHERE email LIKE %:email%",nativeQuery=true)
	User findByEmail(@Param("email")String email);*/
	@Query(value="SELECT * FROM user WHERE email=:email",nativeQuery=true)
	User findByEmail(@Param("email")String email);
	@Query(value="SELECT u FROM user u WHERE u.verificationCode = ?1", nativeQuery = true)
	public User findByVerificationCode(String code);

	@Query(value="SELECT email FROM user u WHERE u.enabled = 1 and u.id = ?1",nativeQuery=true)
	public String getUserEmailByUserId(Long id);

	@Query(value="SELECT * FROM user u WHERE u.enabled = 1 and u.id = ?1",nativeQuery=true)
	public User getUserByUserId(Long id);

	@Query(value="SELECT password FROM user WHERE id=:id",nativeQuery=true)
	User getEncryptedPasswordById(@Param("id")Long id);

	@Modifying
	@Query(value="UPDATE user SET first_name=:firstName, last_name=:lastName, email=:email, campus=:campus, program=:program WHERE id=:id",nativeQuery=true)
	int updateUser(@Param("id")Long id, @Param("firstName")String firstName, @Param("lastName")String lastName, @Param("email")String email, @Param("campus")String campus, @Param("program")String program);

	@Modifying
	@Query(value="UPDATE user SET first_name=:firstName, last_name=:lastName, email=:email, campus=:campus, program=:program, photo=:photo WHERE id=:id",nativeQuery=true)
	int updateUserWithAvatar(@Param("id")Long id, @Param("firstName")String firstName, @Param("lastName")String lastName, @Param("email")String email, @Param("campus")String campus, @Param("program")String program, @Param("photo")byte[] photo);

	@Query(value="SELECT * FROM user WHERE id=?1", nativeQuery = true)
	User findUserById(Long id);
}