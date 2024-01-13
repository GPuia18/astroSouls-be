package com.se.astro;


import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.ZodiacCompatibility;
import com.se.astro.user.model.ZodiacSign;
import com.se.astro.user.model.enums.*;
import com.se.astro.user.repository.AstroUserRepository;
import com.se.astro.user.repository.ZodiacCompatibilityRepository;
import com.se.astro.user.repository.ZodiacRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AstroApplication {

    public static void main(String[] args) {
        SpringApplication.run(AstroApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(AstroUserRepository userRepository, ZodiacRepository zodiacRepository, ZodiacCompatibilityRepository compatibilityRepository) {
        return args -> {
            if (zodiacRepository.count() == 0) {
                zodiacRepository.save(new ZodiacSign("Aries", LocalDateTime.of(2024, 3, 21, 0, 0), LocalDateTime.of(2024, 4, 19, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Taurus", LocalDateTime.of(2024, 4, 20, 0, 0), LocalDateTime.of(2024, 5, 20, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Gemini", LocalDateTime.of(2024, 5, 21, 0, 0), LocalDateTime.of(2024, 6, 20, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Cancer", LocalDateTime.of(2024, 6, 21, 0, 0), LocalDateTime.of(2024, 7, 22, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Leo", LocalDateTime.of(2024, 7, 23, 0, 0), LocalDateTime.of(2024, 8, 22, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Virgo", LocalDateTime.of(2024, 8, 23, 0, 0), LocalDateTime.of(2024, 9, 22, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Libra", LocalDateTime.of(2024, 9, 23, 0, 0), LocalDateTime.of(2024, 10, 22, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Scorpio", LocalDateTime.of(2024, 10, 23, 0, 0), LocalDateTime.of(2024, 11, 21, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Sagittarius", LocalDateTime.of(2024, 11, 22, 0, 0), LocalDateTime.of(2024, 12, 21, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Capricorn", LocalDateTime.of(2024, 12, 22, 0, 0), LocalDateTime.of(2025, 1, 19, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Aquarius", LocalDateTime.of(2025, 1, 20, 0, 0), LocalDateTime.of(2025, 2, 18, 23, 59)));
                zodiacRepository.save(new ZodiacSign("Pisces", LocalDateTime.of(2025, 2, 19, 0, 0), LocalDateTime.of(2025, 3, 20, 23, 59)));
            }

            if (compatibilityRepository.count() == 0) {
                Map<String, Map<String, Integer>> compatibilities = getZodiacCompatibilities();
                compatibilities.forEach((zodiac1, map) ->
                        map.forEach((zodiac2, score) ->
                                compatibilityRepository.save(new ZodiacCompatibility(zodiac1, zodiac2, score))
                        )
                );
            }
        };
    }

    private Map<String, Map<String, Integer>> getZodiacCompatibilities() {
        Map<String, Map<String, Integer>> compatibilities = new HashMap<>();

        // Aries Compatibility
        Map<String, Integer> ariesCompatibility = new HashMap<>();
        ariesCompatibility.put("Aries", 7);
        ariesCompatibility.put("Taurus", 4);
        ariesCompatibility.put("Gemini", 6);
        ariesCompatibility.put("Cancer", 5);
        ariesCompatibility.put("Leo", 8);
        ariesCompatibility.put("Virgo", 5);
        ariesCompatibility.put("Libra", 6);
        ariesCompatibility.put("Scorpio", 7);
        ariesCompatibility.put("Sagittarius", 9);
        ariesCompatibility.put("Capricorn", 5);
        ariesCompatibility.put("Aquarius", 7);
        ariesCompatibility.put("Pisces", 6);
        compatibilities.put("Aries", ariesCompatibility);

        // Taurus Compatibility
        Map<String, Integer> taurusCompatibility = new HashMap<>();
        taurusCompatibility.put("Aries", 4);
        taurusCompatibility.put("Taurus", 7);
        taurusCompatibility.put("Gemini", 5);
        taurusCompatibility.put("Cancer", 8);
        taurusCompatibility.put("Leo", 5);
        taurusCompatibility.put("Virgo", 8);
        taurusCompatibility.put("Libra", 6);
        taurusCompatibility.put("Scorpio", 7);
        taurusCompatibility.put("Sagittarius", 4);
        taurusCompatibility.put("Capricorn", 9);
        taurusCompatibility.put("Aquarius", 5);
        taurusCompatibility.put("Pisces", 7);
        compatibilities.put("Taurus", taurusCompatibility);

        // Gemini Compatibility
        Map<String, Integer> geminiCompatibility = new HashMap<>();
        geminiCompatibility.put("Aries", 6);
        geminiCompatibility.put("Taurus", 5);
        geminiCompatibility.put("Gemini", 7);
        geminiCompatibility.put("Cancer", 5);
        geminiCompatibility.put("Leo", 8);
        geminiCompatibility.put("Virgo", 6);
        geminiCompatibility.put("Libra", 9);
        geminiCompatibility.put("Scorpio", 5);
        geminiCompatibility.put("Sagittarius", 7);
        geminiCompatibility.put("Capricorn", 4);
        geminiCompatibility.put("Aquarius", 9);
        geminiCompatibility.put("Pisces", 6);
        compatibilities.put("Gemini", geminiCompatibility);

        // Cancer Compatibility
        Map<String, Integer> cancerCompatibility = new HashMap<>();
        cancerCompatibility.put("Aries", 5);
        cancerCompatibility.put("Taurus", 8);
        cancerCompatibility.put("Gemini", 5);
        cancerCompatibility.put("Cancer", 7);
        cancerCompatibility.put("Leo", 5);
        cancerCompatibility.put("Virgo", 8);
        cancerCompatibility.put("Libra", 6);
        cancerCompatibility.put("Scorpio", 9);
        cancerCompatibility.put("Sagittarius", 4);
        cancerCompatibility.put("Capricorn", 7);
        cancerCompatibility.put("Aquarius", 4);
        cancerCompatibility.put("Pisces", 9);
        compatibilities.put("Cancer", cancerCompatibility);

        // Leo Compatibility
        Map<String, Integer> leoCompatibility = new HashMap<>();
        leoCompatibility.put("Aries", 8);
        leoCompatibility.put("Taurus", 5);
        leoCompatibility.put("Gemini", 8);
        leoCompatibility.put("Cancer", 5);
        leoCompatibility.put("Leo", 7);
        leoCompatibility.put("Virgo", 5);
        leoCompatibility.put("Libra", 8);
        leoCompatibility.put("Scorpio", 6);
        leoCompatibility.put("Sagittarius", 9);
        leoCompatibility.put("Capricorn", 5);
        leoCompatibility.put("Aquarius", 7);
        leoCompatibility.put("Pisces", 6);
        compatibilities.put("Leo", leoCompatibility);

        // Virgo Compatibility
        Map<String, Integer> virgoCompatibility = new HashMap<>();
        virgoCompatibility.put("Aries", 5);
        virgoCompatibility.put("Taurus", 8);
        virgoCompatibility.put("Gemini", 6);
        virgoCompatibility.put("Cancer", 8);
        virgoCompatibility.put("Leo", 5);
        virgoCompatibility.put("Virgo", 7);
        virgoCompatibility.put("Libra", 6);
        virgoCompatibility.put("Scorpio", 8);
        virgoCompatibility.put("Sagittarius", 5);
        virgoCompatibility.put("Capricorn", 9);
        virgoCompatibility.put("Aquarius", 5);
        virgoCompatibility.put("Pisces", 7);
        compatibilities.put("Virgo", virgoCompatibility);

// Libra Compatibility
        Map<String, Integer> libraCompatibility = new HashMap<>();
        libraCompatibility.put("Aries", 6);
        libraCompatibility.put("Taurus", 6);
        libraCompatibility.put("Gemini", 9);
        libraCompatibility.put("Cancer", 6);
        libraCompatibility.put("Leo", 8);
        libraCompatibility.put("Virgo", 6);
        libraCompatibility.put("Libra", 7);
        libraCompatibility.put("Scorpio", 6);
        libraCompatibility.put("Sagittarius", 7);
        libraCompatibility.put("Capricorn", 6);
        libraCompatibility.put("Aquarius", 9);
        libraCompatibility.put("Pisces", 7);
        compatibilities.put("Libra", libraCompatibility);

// Scorpio Compatibility
        Map<String, Integer> scorpioCompatibility = new HashMap<>();
        scorpioCompatibility.put("Aries", 7);
        scorpioCompatibility.put("Taurus", 7);
        scorpioCompatibility.put("Gemini", 5);
        scorpioCompatibility.put("Cancer", 9);
        scorpioCompatibility.put("Leo", 6);
        scorpioCompatibility.put("Virgo", 8);
        scorpioCompatibility.put("Libra", 6);
        scorpioCompatibility.put("Scorpio", 7);
        scorpioCompatibility.put("Sagittarius", 6);
        scorpioCompatibility.put("Capricorn", 8);
        scorpioCompatibility.put("Aquarius", 6);
        scorpioCompatibility.put("Pisces", 9);
        compatibilities.put("Scorpio", scorpioCompatibility);

        // Sagittarius Compatibility
        Map<String, Integer> sagittariusCompatibility = new HashMap<>();
        sagittariusCompatibility.put("Aries", 9);
        sagittariusCompatibility.put("Taurus", 4);
        sagittariusCompatibility.put("Gemini", 7);
        sagittariusCompatibility.put("Cancer", 4);
        sagittariusCompatibility.put("Leo", 9);
        sagittariusCompatibility.put("Virgo", 5);
        sagittariusCompatibility.put("Libra", 7);
        sagittariusCompatibility.put("Scorpio", 6);
        sagittariusCompatibility.put("Sagittarius", 7);
        sagittariusCompatibility.put("Capricorn", 5);
        sagittariusCompatibility.put("Aquarius", 8);
        sagittariusCompatibility.put("Pisces", 6);
        compatibilities.put("Sagittarius", sagittariusCompatibility);

        // Capricorn Compatibility
        Map<String, Integer> capricornCompatibility = new HashMap<>();
        capricornCompatibility.put("Aries", 5);
        capricornCompatibility.put("Taurus", 9);
        capricornCompatibility.put("Gemini", 4);
        capricornCompatibility.put("Cancer", 7);
        capricornCompatibility.put("Leo", 5);
        capricornCompatibility.put("Virgo", 9);
        capricornCompatibility.put("Libra", 6);
        capricornCompatibility.put("Scorpio", 8);
        capricornCompatibility.put("Sagittarius", 5);
        capricornCompatibility.put("Capricorn", 7);
        capricornCompatibility.put("Aquarius", 7);
        capricornCompatibility.put("Pisces", 8);
        compatibilities.put("Capricorn", capricornCompatibility);

        // Aquarius Compatibility
        Map<String, Integer> aquariusCompatibility = new HashMap<>();
        aquariusCompatibility.put("Aries", 7);
        aquariusCompatibility.put("Taurus", 5);
        aquariusCompatibility.put("Gemini", 9);
        aquariusCompatibility.put("Cancer", 4);
        aquariusCompatibility.put("Leo", 7);
        aquariusCompatibility.put("Virgo", 5);
        aquariusCompatibility.put("Libra", 9);
        aquariusCompatibility.put("Scorpio", 6);
        aquariusCompatibility.put("Sagittarius", 8);
        aquariusCompatibility.put("Capricorn", 7);
        aquariusCompatibility.put("Aquarius", 7);
        aquariusCompatibility.put("Pisces", 6);
        compatibilities.put("Aquarius", aquariusCompatibility);

        // Pisces Compatibility
        Map<String, Integer> piscesCompatibility = new HashMap<>();
        piscesCompatibility.put("Aries", 6);
        piscesCompatibility.put("Taurus", 7);
        piscesCompatibility.put("Gemini", 6);
        piscesCompatibility.put("Cancer", 9);
        piscesCompatibility.put("Leo", 6);
        piscesCompatibility.put("Virgo", 7);
        piscesCompatibility.put("Libra", 7);
        piscesCompatibility.put("Scorpio", 9);
        piscesCompatibility.put("Sagittarius", 6);
        piscesCompatibility.put("Capricorn", 8);
        piscesCompatibility.put("Aquarius", 6);
        piscesCompatibility.put("Pisces", 7);
        compatibilities.put("Pisces", piscesCompatibility);

        return compatibilities;
    }
}
