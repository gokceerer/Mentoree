package com.gokceerer.Mentoree.Repositories.SQL;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.gokceerer.Mentoree.Models.SQL.User;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User,Integer>{
    @Query(value = "SELECT * FROM mentoreedb.user WHERE username = :username", nativeQuery = true)
    User findByUsername(@Param("username") String username);
    boolean existsByUsername(String username);
}
