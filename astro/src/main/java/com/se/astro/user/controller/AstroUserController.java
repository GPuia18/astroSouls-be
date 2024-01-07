package com.se.astro.user.controller;

import com.se.astro.authentication.model.SearchRequest;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.UserSearchRequest;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<AstroUser> fetchUserByUsernameOrEmail(@RequestBody SearchRequest searchRequest) {
        Optional<AstroUser> user = Optional.empty();

        System.out.println(searchRequest);

        if (searchRequest != null) {
            user = astroUserService.getUserByUsername(searchRequest.getSearch());
            if(user.isEmpty()) {
                user = astroUserService.getUserByEmail(searchRequest.getSearch());
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

    @PostMapping("/like")
    public ResponseEntity<?> likeUserByUsernameOrEmail(@RequestBody SearchRequest likeRequest) {

        Optional<AstroUser> principalUser = getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (likeRequest != null) {
            user = astroUserService.getUserByUsername(likeRequest.getSearch());
            if (user.isEmpty()) {
                user = astroUserService.getUserByEmail(likeRequest.getSearch());
            }
        }

        if (user.isPresent()) {

            astroUserService.likeUser(user, principalUser);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/match")
    public ResponseEntity<?> matchUserByUsernameOrEmail(@RequestBody SearchRequest matchRequest) {

        Optional<AstroUser> principalUser = getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (matchRequest != null) {
            user = astroUserService.getUserByUsername(matchRequest.getSearch());
            if (user.isEmpty()) {
                user = astroUserService.getUserByEmail(matchRequest.getSearch());
            }
        }

        if (user.isPresent()) {

            AstroUser user1 = user.get();
            AstroUser user2 = principalUser.get();

            user1.createMatch(user2);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Optional<AstroUser> getPrincipalUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentUsername = userDetails.getUsername();
        }

        return astroUserService.getUserByUsername(currentUsername);
    }
}
