package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.Topic;

import java.util.List;

public interface TopicService {

    List<Topic> getAllTopics();

    List<Topic> getTopicsOfUser(Long id);
}