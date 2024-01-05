package com.se.astro.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ZodiacCompatibility {
    @Id
    private String id;
    private String zodiacName1;
    private String zodiacName2;
    private int compatibility;
}
