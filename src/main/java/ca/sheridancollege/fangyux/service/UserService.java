package ca.sheridancollege.fangyux.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.web.dto.UserRegistrationDto;

@Service
public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registrationDto) throws IOException;

	List<User> getAllUsers();

	User saveUser(User student);

	User getUserById(Long id);

	User updateUser(User user);

	void deleteUserById(Long id);

	UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

	User getCurrentlyLoggedInUser(Authentication authentication);

	User getUserByEmail(String email);

	//public  User getCurrentlyLoggedInUser(Authentication authentication);
}