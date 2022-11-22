package ca.sheridancollege.fangyux.beans;

import javax.persistence.*;

@Entity
@Table(name = "announcement")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "announcement")
    private String announcement;

    @Column(name = "host_name")
    private String hostName;

    @JoinColumn(name = "event_id")
    private Long eventId;


    @JoinColumn(name = "group_id")
    private Long groupId;


    @JoinColumn(name = "user_id")
    private Long userId;
    public Announcement(String announcement, Long eventId, Long groupId, Long userId) {
        super();
        this.announcement = announcement;
        this.eventId = eventId;
        this.groupId = groupId;
        this.userId = userId;
    }

    public Announcement() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
