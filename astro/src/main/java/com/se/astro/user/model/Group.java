package com.se.astro.user.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Group {
    private String name;
    private List<String> members;
}
