package ca.sheridancollege.fangyux.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import ca.sheridancollege.fangyux.Utils.ImageOperation;
import ca.sheridancollege.fangyux.repository.RoleRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
import ca.sheridancollege.fangyux.beans.Role;
import ca.sheridancollege.fangyux.beans.User;
import ca.sheridancollege.fangyux.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private User user;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private PasswordEncoder passwordEncoder1;
	@Autowired
	private JavaMailSender mailSender;

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

	/*@Override
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
	}*/

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		
		 User user = userRepository.findByEmail(email);
		 System.out.println("User email: " + user.getEmail());
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
		//User user = userRepository.findByEmailandEnable(email,true);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),false,true,true,true,
				mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	public User getCurrentlyLoggedInUser(Authentication authentication) {

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
	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);

			return true;
		}
	}
	public User register(User user, String siteURL)
			throws IOException, MessagingException {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);;
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);

		//set role
		long id =2;
		Role role = roleRepository.getById((int) id);
		user.addRole(role);

		//Set default avatar
		File defaultAvatar=new File("src/main/resources/static/img/default_avatar.png");
		String absolutePath = defaultAvatar.getAbsolutePath();
		File a=new File(absolutePath);

		MultipartFile avatar=new MockMultipartFile("avatar", Files.readAllBytes(a.toPath()));

		user= ImageOperation.attatchToUser(user, avatar);

		//userRepository.save(user);

		sendVerificationEmail(user, siteURL);
		return userRepository.save(user);
	}
	public void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {

		String toAddress = user.getEmail();
		String fromAddress = "applicationgroupnow@gmail.com";
		String senderName = "Grouping Now Team";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Grouping Now team.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstName() + user.getLastName());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}

}
