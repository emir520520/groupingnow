package ca.sheridancollege.fangyux.repository;

import ca.sheridancollege.fangyux.beans.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query(value="SELECT * FROM topic",nativeQuery=true)
    List<Topic> getAllTopics();

    @Query(value="SELECT * FROM topic RIGHT JOIN user_topic ON topic_id=user_topic.topic_id WHERE user_topic.user_id=:id", nativeQuery = true)
    List<Topic> getTopicOfUser(@Param("id")Long id);
}