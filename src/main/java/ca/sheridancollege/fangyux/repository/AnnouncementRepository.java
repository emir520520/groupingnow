package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    //public Announcement findByUserAndEventAndGroup(User user, Event event, SchoolGroup group);

    @Query(value="SELECT * FROM announcement c WHERE c.user_id= ?1 AND c.group_id= ?2 AND c.event_id= ?2",nativeQuery=true)
    List<Announcement> getAnnouncementsByUserIdAndGroupIdAndEventId(Long userId, Long groupId, Long eventId);
}
