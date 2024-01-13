package com.se.astro.message.controller;

import com.se.astro.message.dto.Message;
import com.se.astro.message.dto.MessageRequest;
import com.se.astro.message.dto.MessagesBetweenUsersRequest;
import com.se.astro.message.dto.UserMessages;
import com.se.astro.message.service.MessageService;
import com.se.astro.helper.UserPrincipalService;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/message")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("@userPrincipalService.isCurrentUserNotBanned()")
public class MessageController {

    private final UserPrincipalService userPrincipalService;
    private final AstroUserService astroUserService;
    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<AstroUser> sendMessageToUserByUsernameOrEmail(@RequestBody MessageRequest messageRequest) {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (messageRequest != null) {
            user = astroUserService.getUserByUsername(messageRequest.getReceiverUsername());
        }

        if (user.isPresent()) {

            messageService.sendMessage(messageRequest, principalUser.get());

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/all")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@RequestBody MessagesBetweenUsersRequest messagesBetweenUsersRequest) {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (messagesBetweenUsersRequest != null) {
            user = astroUserService.getUserByUsername(messagesBetweenUsersRequest.getUsername());
        }

        if (user.isPresent()) {

            Optional<List<Message>> messages = messageService.getMessagesBetweenUsers(principalUser.get(), user.get());

            if (messages.isPresent()) {
                return ResponseEntity.ok(messages.get());
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all-user-messages")
    public ResponseEntity<List<UserMessages>> getAllMessagesOfUser() {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<UserMessages> messages = messageService.getAllMessagesOfUser(principalUser.get());

        if (!messages.isEmpty()) {
            return ResponseEntity.ok(messages);
        }

        return ResponseEntity.notFound().build();
    }
}
