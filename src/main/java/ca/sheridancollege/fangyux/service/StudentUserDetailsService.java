package ca.sheridancollege.fangyux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
//@Transactional
public class StudentUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		System.out.println("email in student service " + user.getEmail());
		if(user == null)
		{
			System.out.println("Could not find user with email: " + email);
			throw new UsernameNotFoundException("Could not find user with email: " + email);
		}
		else{
			System.out.println("user is not null");
		}
		return new StudentUserDetails(user);
	}

}
