package com.se.astro.user.repository;


import com.se.astro.user.model.AstroUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AstroUserRepository extends MongoRepository<AstroUser, String> {
    Optional<AstroUser> findByUsername(String username);
    Optional<AstroUser> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("{'username': {$in: ?0}}")
    Optional<List<AstroUser>> findAllByUsername(List<String> usernames);
}
