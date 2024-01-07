package com.se.astro.message.service;

import com.se.astro.message.model.Message;
import com.se.astro.message.model.MessageRequest;
import com.se.astro.message.repository.MessageRepository;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.repository.AstroUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message sendMessage(MessageRequest request, AstroUser principalUser) {
        var message = Message.builder()
                .content(request.getContent())
                .senderUsername(principalUser.getUsername())
                .receiverUsername(request.getReceiverUsername())
                .sendingTime(LocalDateTime.now())
                .build();

        messageRepository.save(message);

        return message;
    }
}
