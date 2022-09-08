package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query(value="SELECT * FROM topic",nativeQuery=true)
    List<Topic> getAllTopics();
}