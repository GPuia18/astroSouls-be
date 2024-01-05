package com.se.astro.user.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Message {
    @Id
    private String id;
    private LocalDateTime sendingTime;
    private String receiver;
}
