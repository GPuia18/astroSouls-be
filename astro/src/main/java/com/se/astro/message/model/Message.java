package com.se.astro.message.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    private String id;
    private LocalDateTime sendingTime;
    private String content;
    private String senderUsername;
    private String receiverUsername;
    private String image;
}
