package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CartGroupEventRepository extends JpaRepository<CartEventGroup, Long> {

    public CartEventGroup findByUserAndEventAndGroup(User user, Event event, SchoolGroup group);

    @Query(value="SELECT group_id FROM cart_groups_events c WHERE c.user_id= ?1 AND c.event_id= ?2",nativeQuery=true)
    List<Long> getGroupIdByUserIdAndEventId(Long userId,Long eventId);

    @Query(value="SELECT DISTINCT group_id FROM cart_groups_events c WHERE c.user_id= ?1",nativeQuery=true)
    List<Long> getGroupIdsByUserId(Long userId);

    @Query(value="SELECT DISTINCT user_id FROM cart_groups_events c WHERE c.group_id= ?1 AND c.event_id= ?2",nativeQuery=true)
    List<Long> getAllUserIdByGroupIdAndEventId(Long groupId, Long eventId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM cart_groups_events WHERE user_id = ?1 AND group_id = ?2 AND event_id = ?3",nativeQuery=true)
    public void deleteAllByUserIdAndGroupIdAndEventId(Long userId, Long groupId, Long eventId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM cart_groups_events WHERE user_id = ?1 AND group_id = ?2",nativeQuery=true)
    public void deleteAllByUserIdAndGroupId(Long userId, Long groupId);
}
