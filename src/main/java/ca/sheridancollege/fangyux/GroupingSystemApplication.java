package ca.sheridancollege.fangyux;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GroupingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupingSystemApplication.class, args);
	}
	
	
}