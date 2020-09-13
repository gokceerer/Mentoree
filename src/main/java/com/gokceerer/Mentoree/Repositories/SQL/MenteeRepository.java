package com.gokceerer.Mentoree.Repositories.SQL;

import com.gokceerer.Mentoree.Models.SQL.Mentee;
import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.data.repository.CrudRepository;

public interface MenteeRepository extends CrudRepository<Mentee,Integer> {
    boolean existsByUser(User user);
    Mentee findByUser(User user);
}