package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private String username;
    private Boolean isMentor;
    private Boolean isMentee;

    @OneToMany(mappedBy = "user" )
    private List<Mentor> mentoring;

    @OneToMany(mappedBy = "user")
    private List<Mentee> menteeing;

    public List<MentorApplication> getMentorshipApplications() {
        return mentorshipApplications;
    }

    @OneToMany(mappedBy = "user")
    private List<MentorApplication> mentorshipApplications;


    public List<Mentor> getMentoring() {
        return mentoring;
    }

    public List<Mentee> getMenteeing() {
        return menteeing;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    private Boolean isAdmin;

    public Boolean isMentor() {
        return isMentor;
    }

    public void setMentor(Boolean mentor) {
        isMentor = mentor;
    }

    public Boolean isMentee() {
        return isMentee;
    }

    public void setMentee(Boolean mentee) {
        isMentee = mentee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
