package ca.sheridancollege.fangyux.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;


import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

//@ComponentScan
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	private String course="";
	
	private String topic;
	
	private String program;
	
	private String password;
	
	private boolean enabled;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<SchoolGroup> groups;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(
					name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(
					name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_event")
	private List<Event> events;
	
	@OneToMany
	private List<Course> courseList = new ArrayList<Course>();
	
	@OneToMany
	private List<Topic> topicList = new ArrayList<Topic>();
	
	@Lob
	@Column(name = "photo", nullable = true)
	private byte[] photo;
	
	@Transient
	String base64Encoded;
	

	public User()
	{
		
	}
	
	public User(String firstName, String lastName, String email, String password, Collection<Role> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<Course> getCourseList(){
		return this.courseList;
	}
	
	public void setProgram(String program) {
		this.program = program;
	}
	
	public String getProgram() {
		return program;
	}
	
	public void setPhoto(byte[]  photo) {
		this.photo = photo;
	}
	
	public List<Topic> getTopicList(){
		return this.topicList;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public void appendCourse(String course) {
		this.course += course;
	}
	
	public byte[]  getPhoto() {
		return photo;
	}
	
	public void setBase64Encoded(String b) {
		base64Encoded=b;
	}
	
	public String getBase64Encoded() {
		return base64Encoded;
	}
}
