package com.gokceerer.Mentoree.Models.NOSQL;

import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Language;

import javax.persistence.Id;
import java.util.List;

@Document(collection = "Mentor", language = "tr")
public class MentorNOSQL {
    @Id
    private String _id;
    private String name;
    private String surname;
    private String username;
    private String topic;
    private String subtopic;
    @TextIndexed private String description;

    public MentorNOSQL(String name, String surname, String username, String topic, String subtopic, String description){
        this.name=name;
        this.surname=surname;
        this.username=username;
        this.topic = topic;
        this.subtopic = subtopic;
        this.description = description;
    }
    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public String getDescription() {
        return description;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

}
