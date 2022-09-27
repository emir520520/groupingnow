package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartGroupRepository extends JpaRepository<CartGroup, Long> {
    public List<CartGroup> findByUser(User user);


    public CartGroup findByUserAndGroup(User user, SchoolGroup group);



    @Modifying
    @Query("Update CartGroup c SET c.participants = ?1 WHERE c.user.id = ?2 AND c.group.id = ?3")
    public void updateQuantity(Integer participants, Integer userId, Integer groupId);

    @Modifying
    @Query("DELETE FROM CartGroup c WHERE c.user.id = ?1 AND c.group.id = ?2")
    public void deleteByUserAndGroup(User user, SchoolGroup group);
}
