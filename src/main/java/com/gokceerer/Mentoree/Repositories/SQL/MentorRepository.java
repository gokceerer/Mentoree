package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.Mentor;
import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MentorRepository extends CrudRepository<Mentor,Integer> {
    boolean existsMentorBySubtopicAndTopic(Subtopic subtopic, Topic topic);
    boolean existsMentorByTopic(Topic topic);
    boolean existsMentorByUserAndTopicAndSubtopic(User user, Topic topic, Subtopic subtopic);
    Mentor findByUserAndTopicAndSubtopic(User user, Topic topic, Subtopic subtopic);
    Mentor findByNoSqlId(String noSqlId);
    List<Mentor> findAllByUser(User user);
}
