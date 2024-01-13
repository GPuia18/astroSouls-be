package com.se.astro.message.service;

import com.se.astro.message.dto.Message;
import com.se.astro.message.dto.MessageRequest;
import com.se.astro.message.dto.UserMessages;
import com.se.astro.message.repository.MessageRepository;
import com.se.astro.user.model.AstroUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<List<Message>> getMessagesBetweenUsers(AstroUser user1, AstroUser user2) {
        return messageRepository.findAllByUsers(user1.getUsername(), user2.getUsername());
    }

    public Optional<UserMessages> getAllMessagesOfUser(AstroUser user) {
        Optional<List<Message>> messages = messageRepository.findAllByUser(user.getUsername());
        if (messages.isPresent()) {
            return Optional.of(new UserMessages(user.getUsername(), messages.get()));
        } else {
            return Optional.empty();
        }
    }
}
