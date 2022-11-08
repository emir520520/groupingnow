package ca.sheridancollege.fangyux.beans;

import javax.persistence.*;

@Entity
@Table(name = "cart_groups_events")
public class CartEventGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private SchoolGroup group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SchoolGroup getGroup() {
        return group;
    }

    public void setGroup(SchoolGroup group) {
        this.group = group;
    }
}
