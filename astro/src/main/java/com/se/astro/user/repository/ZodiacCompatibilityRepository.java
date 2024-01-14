package com.se.astro.user.repository;

import com.se.astro.user.model.ZodiacCompatibility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ZodiacCompatibilityRepository extends MongoRepository<ZodiacCompatibility, String> {
    @Query("{ 'zodiacName1': ?0, 'zodiacName2': ?1 }")
    Optional<ZodiacCompatibility> findCompatibilityBetween(String zodiacName1, String zodiacName2);

    @Query("{ $or: [ { 'zodiacName1': ?0 }, { 'zodiacName2': ?0 } ] }")
    Optional<List<ZodiacCompatibility>> findAllByZodiacName(String zodiacName);
}
