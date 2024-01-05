package com.se.astro.user.model;

import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchRequest {
    private Double minHeight;
    private Double maxHeight;
    private List<Tag> tags;
    private List<Gender> searchingFor;
    private Integer minAge;
    private Integer maxAge;
    private String zodiacSign;
}
