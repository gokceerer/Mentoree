package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.*;
import org.springframework.data.repository.CrudRepository;

public interface MentorshipRepository extends CrudRepository<Mentorship,Integer> {
    boolean existsMentorshipBySubtopicAndTopic(Subtopic subtopic, Topic topic);
    boolean existsMentorshipByTopic(Topic topic);
    boolean existsMentorshipBySubtopic(Subtopic subtopic);
    boolean existsMentorshipByMenteeAndTopicAndFinished(Mentee mentee, Topic topic, Boolean isFinished);
    Mentorship findByMenteeAndTopic(Mentee mentee, Topic topic);
    Mentorship findByMentorAndMenteeAndTopicAndSubtopic(Mentor mentor, Mentee mentee, Topic topic, Subtopic subtopic);
    boolean existsMentorshipByMentorAndMenteeAndTopicAndSubtopic(Mentor mentor, Mentee mentee, Topic topic, Subtopic subtopic);
}
