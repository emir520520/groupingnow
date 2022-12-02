package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.Role;
import ca.sheridancollege.fangyux.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    //Role findByRole(String role);
    @Query(value="SELECT * FROM role WHERE name LIKE %:name%",nativeQuery=true)
    Role findByRole(@Param("name")String name);
}
