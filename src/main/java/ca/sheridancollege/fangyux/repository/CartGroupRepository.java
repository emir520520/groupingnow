package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartGroupRepository extends JpaRepository<CartGroup, Long> {
    public List<CartGroup> findByUser(User user);


    public CartGroup findByUserAndGroup(User user, SchoolGroup group);

    @Modifying
    @Query("Update CartGroup c SET c.participants = ?1 WHERE c.user.id = ?2 AND c.group.id = ?3")
    public void updateQuantity(Integer participants, Integer userId, Integer groupId);

    @Modifying
    @Transactional
    @Query(value ="DELETE FROM cart_groups WHERE user_id = ?1 AND group_id = ?2",nativeQuery = true)
    public void deleteByUserAndGroup(Long user, Long group);

    @Query(value ="SELECT COUNT(*) FROM cart_groups WHERE group_id=?1 AND user_id=?2", nativeQuery = true)
    public int checkCartGroupOfUser(Long groupId, Long userId);

    @Query(value ="SELECT user_id FROM cart_groups WHERE group_id=?1", nativeQuery = true)
    public List<Long> selectAllUserIdByGroupId(Long groupId);
}
