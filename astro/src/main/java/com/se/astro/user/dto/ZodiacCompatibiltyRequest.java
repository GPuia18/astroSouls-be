package com.se.astro.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ZodiacCompatibiltyRequest {
    private String zodiac1;
    private String zodiac2;
}
