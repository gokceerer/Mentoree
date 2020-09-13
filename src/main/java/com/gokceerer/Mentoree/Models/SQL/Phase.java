package com.gokceerer.Mentoree.Models.SQL;

import com.gokceerer.Mentoree.Models.SQL.Mentorship;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Phase {
    public Integer getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="mentorship_id")
    private Mentorship mentorship;

    String name;
    int mentorRating;
    int menteeRating;
    String mentorEvaluation;
    String menteeEvaluation;
    boolean mentorEvaluated = false;

    public boolean isMentorEvaluated() {
        return mentorEvaluated;
    }

    public void setMentorEvaluated(boolean mentorEvaluated) {
        this.mentorEvaluated = mentorEvaluated;
    }

    public void setMenteeEvaluated(boolean menteeEvaluated) {
        this.menteeEvaluated = menteeEvaluated;
    }

    public boolean isMenteeEvaluated() {
        return menteeEvaluated;
    }

    boolean menteeEvaluated = false;

    public void setMentorRating(int mentorRating) {
        this.mentorRating = mentorRating;
    }

    public void setMenteeRating(int menteeRating) {
        this.menteeRating = menteeRating;
    }

    public void setMentorEvaluation(String mentorEvaluation) {
        this.mentorEvaluation = mentorEvaluation;
    }

    public void setMenteeEvaluation(String menteeEvaluation) {
        this.menteeEvaluation = menteeEvaluation;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    Date startTime;
    Date endTime;
    String status;

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
       return endTime;
    }

    public Mentorship getMentorship() {
        return mentorship;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMentorship(Mentorship mentorship) {
        this.mentorship = mentorship;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getMentorRating() {
        return mentorRating;
    }

    public int getMenteeRating() {
        return menteeRating;
    }

    public String getMentorEvaluation() {
        return mentorEvaluation;
    }

    public String getMenteeEvaluation() {
        return menteeEvaluation;
    }

    public String getStatus() {
        return status;
    }


}
