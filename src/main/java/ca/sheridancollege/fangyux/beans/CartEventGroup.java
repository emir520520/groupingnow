package ca.sheridancollege.fangyux.beans;

import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;

@Entity
@Table(name = "cart_groups_events")
public class CartEventGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long event;

    @Column(name = "group_id")
    private Long group;

    @Column(name = "user_id")
    private Long user;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEvent() {
        return event;
    }

    public void setEvent(Long event) {
        this.event = event;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }
}
