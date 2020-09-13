package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "subtopic_id")
    private Subtopic subtopic;

    @OneToMany(mappedBy = "mentor")
    private List<Mentorship> mentorships;

    private String description;
    private Integer menteeCount;
    private String noSqlId;

    //Getters
    public User getUser()
    {
        return user;
    }
    public List<Mentorship> getMentorships() {
        return mentorships;
    }
    public Topic getTopic() {
        return topic;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }
    public String getDescription() {
        return description;
    }
    public Integer getMenteeCount() {
        return menteeCount;
    }

    public String getNosql_id() {
        return noSqlId;
    }
    //Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public void setMenteeCount(Integer menteeCount) {
        this.menteeCount = menteeCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setNosql_id(String nosql_id) {
        this.noSqlId = nosql_id;
    }

}
