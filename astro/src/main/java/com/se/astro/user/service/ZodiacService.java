package com.se.astro.user.service;

import com.se.astro.user.model.ZodiacCompatibility;
import com.se.astro.user.model.ZodiacSign;
import com.se.astro.user.repository.ZodiacCompatibilityRepository;
import com.se.astro.user.repository.ZodiacRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ZodiacService {

    private final ZodiacRepository zodiacRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;
    private final MongoTemplate mongoTemplate;

    public List<ZodiacSign> getAllZodiacs() {
        return zodiacRepository.findAll();
    }

    public Optional<ZodiacCompatibility> getCompatibilityBetween(String zodiacSign1, String zodiacSign2) {
        return compatibilityRepository.findCompatibilityBetween(zodiacSign1, zodiacSign2);
    }

    public Optional<List<ZodiacCompatibility>> findCompatibilitiesByZodiacName(String zodiacName) {
        return compatibilityRepository.findAllByZodiacName(zodiacName);
    }
}
