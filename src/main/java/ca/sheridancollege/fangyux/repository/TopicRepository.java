package ca.sheridancollege.fangyux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.fangyux.beans.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
