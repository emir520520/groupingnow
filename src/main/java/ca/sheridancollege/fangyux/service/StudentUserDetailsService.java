package ca.sheridancollege.fangyux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;

public class StudentUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userrepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userrepository.findByEmail(email);
		if(user == null)
			throw new UsernameNotFoundException("User not found");
		return new StudentUserDetails(user);
	}

}
