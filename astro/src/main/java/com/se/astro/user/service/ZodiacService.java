package com.se.astro.user.service;

import com.se.astro.user.model.ZodiacCompatibility;
import com.se.astro.user.model.ZodiacSign;
import com.se.astro.user.repository.ZodiacCompatibilityRepository;
import com.se.astro.user.repository.ZodiacRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ZodiacService {

    private final ZodiacRepository zodiacRepository;
    private final ZodiacCompatibilityRepository compatibilityRepository;

    public List<ZodiacSign> getAllZodiacs() {
        return zodiacRepository.findAll();
    }

    public Optional<ZodiacCompatibility> getCompatibilityBetween(String zodiacSign1, String zodiacSign2) {
        Optional<ZodiacCompatibility> c = compatibilityRepository.findCompatibilityBetween(zodiacSign1, zodiacSign2);
        System.out.println(c.get());
        return c;
    }
}
