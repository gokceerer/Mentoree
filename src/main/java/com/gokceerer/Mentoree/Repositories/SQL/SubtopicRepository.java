package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.Subtopic;
import com.gokceerer.Mentoree.Models.SQL.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubtopicRepository extends CrudRepository<Subtopic,Integer> {
    @Query(value = "SELECT * FROM mentoreedb.subtopic WHERE topic_id = :topic_id", nativeQuery = true)
    Iterable<Subtopic> findByMainTopicId(@Param("topic_id") String topic_id);

    boolean existsSubtopicByNameAndMainTopic(String name, Topic topic_id);
}
