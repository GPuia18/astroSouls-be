package com.se.astro.user.dto;

import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Language;
import com.se.astro.user.model.enums.Nationality;
import com.se.astro.user.model.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    private String zodiacSign;
    private Gender gender;
    private List<Gender> searchingFor;
    private Double height;
    private Nationality nationality;
    private List<Language> language;
    private String header;
    private String description;
    private Integer ageRangeMin;
    private Integer ageRangeMax;
    private List<Tag> tags;
    private List<String> images;
}
