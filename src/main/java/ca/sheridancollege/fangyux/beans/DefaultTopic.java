package ca.sheridancollege.fangyux.beans;

import java.util.ArrayList;
import java.util.List;

public class DefaultTopic {
	
	public List<Topic> LoadList() {
		
		List<Topic> topicList = new ArrayList<Topic>();
		Topic topic = new Topic((long)1, "Java");
		Topic topic2 = new Topic((long)2, "Study Group");
		Topic topic3 = new Topic((long)3, "Basketball");
		Topic topic4 = new Topic((long)4, "Q&A");
		Topic topic5 = new Topic((long)5, "Web Development");
		Topic topic6 = new Topic((long)6, "Music");
		Topic topic7 = new Topic((long)7, "Database");
		Topic topic8 = new Topic((long)8, "Robotics");
		Topic topic9 = new Topic((long)9, "Seminar");
		Topic topic10 = new Topic((long)10, "Baseball");
		
		topicList.add(topic);
		topicList.add(topic2);
		topicList.add(topic3);
		topicList.add(topic4);
		topicList.add(topic5);
		topicList.add(topic6);
		topicList.add(topic7);
		topicList.add(topic8);
		topicList.add(topic9);
		topicList.add(topic10);
		
		return topicList;
	}
	
}
