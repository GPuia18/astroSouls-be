package com.se.astro.authentication.controller;

import com.se.astro.authentication.model.AuthenticationRequest;
import com.se.astro.authentication.model.AuthenticationResponse;
import com.se.astro.authentication.model.RegisterRequest;
import com.se.astro.authentication.service.AuthenticationService;
import com.se.astro.user.model.AstroUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse authenticationResponse = service.register(request);
        if(authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        }
        else{
            return ResponseEntity.badRequest().body("Username or email already exists");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
