package com.se.astro.user.controller;

import com.se.astro.user.dto.ZodiacCompatibiltyRequest;
import com.se.astro.user.model.ZodiacCompatibility;
import com.se.astro.user.model.ZodiacSign;
import com.se.astro.user.service.ZodiacService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/zodiac")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("@userPrincipalService.isCurrentUserNotBanned()")
public class ZodiacController {
    private final ZodiacService zodiacService;

    @GetMapping
    public List<ZodiacSign> fetchAllZodiacs() {
        return zodiacService.getAllZodiacs();
    }

    @PostMapping("/compatibility")
    public ResponseEntity<ZodiacCompatibility> fetchZodiacCompatibility(@RequestBody ZodiacCompatibiltyRequest zodiacCompatibiltyRequest) {

        Optional<ZodiacCompatibility> zodiacCompatibility = zodiacService.getCompatibilityBetween(zodiacCompatibiltyRequest.getZodiac1(), zodiacCompatibiltyRequest.getZodiac2());

        if (zodiacCompatibility.isPresent()) {
            return ResponseEntity.ok(zodiacCompatibility.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
