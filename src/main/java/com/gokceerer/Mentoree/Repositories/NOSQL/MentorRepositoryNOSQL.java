package com.gokceerer.Mentoree.Repositories.NOSQL;

import com.gokceerer.Mentoree.Models.NOSQL.MentorNOSQL;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MentorRepositoryNOSQL extends MongoRepository<MentorNOSQL,String> {
    List<MentorNOSQL> findAllByTopic(String topic);
    List<MentorNOSQL> findAllBySubtopic(String subtopic);
}
