package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.MentorApplication;
import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.data.repository.CrudRepository;

public interface MentorApplicationRepository extends CrudRepository<MentorApplication,Integer> {
    boolean existsMentorApplicationByUserAndTopicAndSubtopic(User u, Topic topic, Subtopic subtopic);
}
