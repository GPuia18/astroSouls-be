package com.se.astro.group.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Group {
    private String name;
    private List<String> members;
}
