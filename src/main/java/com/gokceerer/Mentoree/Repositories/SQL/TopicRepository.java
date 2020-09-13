package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic,Integer> {
    boolean existsTopicByName(String name);
}
