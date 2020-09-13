package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;
import java.util.List;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "topic")
    private List<MentorApplication> applicationsWithTopic;

    @OneToMany(mappedBy = "topic")
    private List<Mentor> mentorsWithTopic;

    @OneToMany(mappedBy = "topic")
    private List<Mentorship> onGoingMentorshipsWithTopic;

    @OneToMany(mappedBy = "mainTopic")
    private List<Subtopic> subtopics;

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Subtopic> getSubtopics() {
        return subtopics;
    }

}
