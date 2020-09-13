package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;

@Entity
@Table(name = "mentor_application")
public class MentorApplication {
    public enum ApplicationState{
        ACCEPTED("Kabul edildi"),
        REJECTED("Reddedildi"),
        PENDING("Beklemede");

        private String displayValue;

        private ApplicationState(String displayName){
            this.displayValue = displayName;
        }

        public String getDisplayValue(){
            return displayValue;
        }
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public User getUser() {
        return user;
    }
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "subtopic_id")
    private Subtopic subtopic;

    private ApplicationState state = ApplicationState.PENDING;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Integer getId() {
        return id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }

    public ApplicationState getState() {
        return state;
    }

    public void setState(ApplicationState state) {
        this.state = state;
    }


}
