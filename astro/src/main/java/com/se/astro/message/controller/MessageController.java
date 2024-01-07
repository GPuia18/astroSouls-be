package com.se.astro.message.controller;

import com.se.astro.message.model.Message;
import com.se.astro.message.model.MessageRequest;
import com.se.astro.message.service.MessageService;
import com.se.astro.user.model.SearchRequest;
import com.se.astro.helper.UserPrincipalService;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/message")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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
}
