package ca.sheridancollege.fangyux.beans;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name="school_group")
public class SchoolGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NonNull
	private Long userId;
	@NonNull
	private String name;
	@NonNull
	private String category;
	@NonNull
	private String study;

	@NonNull
	private String admins;

	@NonNull
	@Column(name = "admin_id")
	private Long admin_id;
	private String invites;
	@NonNull
	private String description;
	private Boolean chat;

	@Lob
	@Column(name = "photo", nullable = true)
	private byte[] photo;
	
	@Transient
	String base64Encoded;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy="groups")
	private List<User> users;

//	@ManyToOne
}
