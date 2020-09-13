package com.gokceerer.Mentoree.Models.SQL;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mentee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "mentee" )
    private List<Mentorship> mentorships;

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Mentorship> getMentorships() {
        return mentorships;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
