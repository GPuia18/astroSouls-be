package com.se.astro.authentication.dto;

import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Language;
import com.se.astro.user.model.enums.Nationality;
import com.se.astro.user.model.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private LocalDateTime birthday;
    private String zodiacSign;
    private Gender gender;
    private List<Gender> searchingFor;
    private double height;
    private Nationality nationality;
    private List<Language> language;
    private String header;
    private String description;
    private int ageRangeMin;
    private int ageRangeMax;
    private List<Tag> tags;
    private List<String> images;
}
