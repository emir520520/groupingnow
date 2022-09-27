package ca.sheridancollege.fangyux.beans;

import javax.persistence.*;

@Entity
@Table(name = "cart_groups")
public class CartGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private SchoolGroup group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int participants;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolGroup getGroup() {
        return group;
    }

    public void setGroup(SchoolGroup group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
