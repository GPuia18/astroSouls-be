package com.se.astro.message.service;

import com.se.astro.message.dto.MessageRequestWithImage;
import com.se.astro.message.model.Message;
import com.se.astro.message.dto.MessageRequest;
import com.se.astro.message.dto.UserMessages;
import com.se.astro.message.repository.MessageRepository;
import com.se.astro.user.model.AstroUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public Message sendMessageWithImage(MessageRequestWithImage request, AstroUser principalUser) {
        var message = Message.builder()
                .content(request.getContent())
                .senderUsername(principalUser.getUsername())
                .receiverUsername(request.getReceiverUsername())
                .sendingTime(LocalDateTime.now())
                .image(request.getImage())
                .build();

        messageRepository.save(message);

        return message;
    }

    public Optional<List<Message>> getMessagesBetweenUsers(AstroUser user1, AstroUser user2) {
        return messageRepository.findAllByUsers(user1.getUsername(), user2.getUsername());
    }

    public List<UserMessages> getAllMessagesOfUser(AstroUser user) {
        Optional<List<Message>> messagesOpt = messageRepository.findAllByUser(user.getUsername());
        if (!messagesOpt.isPresent()) {
            return Collections.emptyList();
        }

        List<Message> messages = messagesOpt.get();
        Map<String, List<Message>> messagesByOtherUser = new HashMap<>();

        for (Message message : messages) {
            String otherUsername = message.getSenderUsername().equals(user.getUsername())
                    ? message.getReceiverUsername() : message.getSenderUsername();

            messagesByOtherUser.computeIfAbsent(otherUsername, k -> new ArrayList<>()).add(message);
        }

        return messagesByOtherUser.entrySet().stream()
                .map(entry -> new UserMessages(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
