package com.se.astro.message.dto;

import com.se.astro.message.model.Message;

import java.util.List;

public class UserMessages {
    private String username;
    private List<Message> messages;

    public UserMessages(String username, List<Message> messages) {
        this.username = username;
        this.messages = messages;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
