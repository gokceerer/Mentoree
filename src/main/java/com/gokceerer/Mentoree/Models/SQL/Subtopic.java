package com.gokceerer.Mentoree.Models.SQL;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Subtopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic mainTopic;

    @OneToMany(mappedBy = "subtopic")
    private List<MentorApplication> applicationsWithSubtopic;

    @OneToMany(mappedBy = "subtopic")
    private List<Mentor> mentorsWithSubtopic;

    @OneToMany(mappedBy = "topic")
    private List<Mentorship> onGoingMentorshipsWithSubtopic;

    public Integer getId() {
        return id;
    }

    public void setMainTopic(Topic mainTopic) {
        this.mainTopic = mainTopic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Topic getMainTopic() {
        return mainTopic;
    }

    public String getName() {
        return name;
    }

    private String name;

}
