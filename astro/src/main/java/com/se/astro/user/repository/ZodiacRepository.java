package com.se.astro.user.repository;

import com.se.astro.user.model.ZodiacSign;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZodiacRepository extends MongoRepository<ZodiacSign, String> {

}
