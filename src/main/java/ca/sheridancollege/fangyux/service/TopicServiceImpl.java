package ca.sheridancollege.fangyux.service;

import ca.sheridancollege.fangyux.beans.Topic;
import ca.sheridancollege.fangyux.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService{

    @Autowired
    private TopicRepository topicRepo;

    @Override
    public List<Topic> getAllTopics() {
        return topicRepo.getAllTopics();
    }

    @Override
    public List<Topic> getTopicsOfUser(Long id) {
        return topicRepo.getTopicOfUser(id);
    }
}