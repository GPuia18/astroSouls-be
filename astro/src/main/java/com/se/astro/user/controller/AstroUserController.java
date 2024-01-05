package com.se.astro.user.controller;

import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.UserSearchRequest;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class AstroUserController {
    private final AstroUserService astroUserService;

    @GetMapping
    public List<AstroUser> fetchAllUsers() {
        return astroUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AstroUser> fetchUserById(@PathVariable String id) {
        Optional<AstroUser> user = astroUserService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/search")
    public ResponseEntity<AstroUser> fetchUserByUsernameOrEmail(@RequestBody String searchRequest) {
        Optional<AstroUser> user = Optional.empty();
        if (searchRequest != null) {
            user = astroUserService.getUserByUsername(searchRequest);
            if(user.isEmpty()) {
                user = astroUserService.getUserByEmail(searchRequest);
            }
        }
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/filter-search")
    public ResponseEntity<?> fetchUsersWithFilters(@RequestBody UserSearchRequest searchRequest) {
        List<AstroUser> users = astroUserService.findUsersWithFilters(searchRequest);
        return ResponseEntity.ok(users);
    }
}
