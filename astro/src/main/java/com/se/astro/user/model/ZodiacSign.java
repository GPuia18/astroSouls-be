package com.se.astro.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class ZodiacSign {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    public ZodiacSign(String name, LocalDateTime periodStart, LocalDateTime periodEnd) {
        this.name = name;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }
}
