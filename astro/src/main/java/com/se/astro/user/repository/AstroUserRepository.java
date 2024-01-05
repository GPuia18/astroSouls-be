package com.se.astro.user.repository;


import com.se.astro.user.model.AstroUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AstroUserRepository extends MongoRepository<AstroUser, String> {
    Optional<AstroUser> findByUsername(String username);
    Optional<AstroUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
