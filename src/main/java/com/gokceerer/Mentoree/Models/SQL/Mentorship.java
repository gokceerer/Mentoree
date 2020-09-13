package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Mentorship {
    public Integer getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @ManyToOne
    @JoinColumn(name = "subtopic_id")
    private Subtopic subtopic;

    private String currentPhase;
    private Date startDate;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    private boolean finished = false;

    public String getStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(startDate == null){
            return "";
        }
        return sdf.format(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Mentor getMentor() {
        return mentor;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public String getCurrentPhase() {
        return currentPhase;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setSubtopic(Subtopic subtopic) {
        this.subtopic = subtopic;
    }

    public void setCurrentPhase(String phase) {
        this.currentPhase = phase;
    }

    public Subtopic getSubtopic() {
        return subtopic;
    }


    public List<Phase> getMentorshipPhases() {
        return mentorshipPhases;
    }

    @OneToMany(mappedBy = "mentorship")
    private List<Phase> mentorshipPhases;


}
