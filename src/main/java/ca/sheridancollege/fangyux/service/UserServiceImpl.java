package ca.sheridancollege.fangyux.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
import ca.sheridancollege.fangyux.beans.Role;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;
import ca.sheridancollege.fangyux.web.dto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private User user;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).get();
	}
	
	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String getUserEncryptedPasswordById(Long id) {
		return String.valueOf(userRepository.getEncryptedPasswordById(id));
	}

	@Override
	public int updateUser(User user) {
		return userRepository.updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCampus(), user.getProgram());
	}

	@Override
	public int updateUserWithAvatar(User user) {
		return userRepository.updateUserWithAvatar(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCampus(), user.getProgram(), user.getPhoto());
	}

	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public User save(UserRegistrationDto registrationDto) throws IOException {
		User student = new User(registrationDto.getFirstName(), registrationDto.getLastName(),
				registrationDto.getEmail(), passwordEncoder.encode(registrationDto.getPassword()),
				Arrays.asList(new Role("ROLE_USER")));

		//Set default avatar
		File defaultAvatar=new File("src/main/resources/static/img/default_avatar.png");
		String absolutePath = defaultAvatar.getAbsolutePath();
		File a=new File(absolutePath);

		MultipartFile avatar=new MockMultipartFile("avatar", Files.readAllBytes(a.toPath()));

		user= ImageOperation.attatchToUser(student, avatar);

		return userRepository.save(student);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		
		 User user = userRepository.findByEmail(email); 
		 if(user == null) 
		 { 
			 throw new UsernameNotFoundException("Invalid email or password."); 
		 } 
		 //return new StudentUserDetails(user);
		 return new org.springframework.security.core.userdetails.User(user.getEmail(),
		 user.getPassword(), mapRolesToAuthorities(user.getRoles()));

	}

	
	 private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) 
	 { 
		 return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()); 
	 }

	

	@Override
	public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	public User getCurrentlyLoggedInUser(Authentication authentication) {
		
		
		/*
		 * if(authentication == null) { System.out.printf("\n No User accessed \n");
		 * return null; } User user = null; Object principal =
		 * authentication.getPrincipal(); if (principal instanceof StudentUserDetails) {
		 * user = ((StudentUserDetails)principal).getUser();
		 * System.out.printf("\n User 1 \n"); } else if(principal instanceof
		 * StudentUserDetails) { String email = ((StudentUserDetails)
		 * principal).getEmail(); user = userRepository.findByEmail(email);
		 * System.out.printf("\n User 2 \n"); } return user;
		 */
		 
		//Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();

		Object principal = null;
     
		if (authentication != null) {
        principal = authentication.getPrincipal();
		}
     
		if (principal != null && principal instanceof StudentUserDetails) {
			StudentUserDetails userDetails = (StudentUserDetails) principal;
        return userDetails.getUser();
		}
     
		return null;

	}

}
