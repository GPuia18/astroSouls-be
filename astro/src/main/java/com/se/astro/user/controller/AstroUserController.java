package com.se.astro.user.controller;

import com.se.astro.user.model.SearchRequest;
import com.se.astro.helper.UserPrincipalService;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.FilterSearchRequest;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AstroUserController {
    private final AstroUserService astroUserService;
    private final UserPrincipalService userPrincipalService;

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

    @PostMapping("/liked-users")
    public ResponseEntity<List<AstroUser>> fetchLikedUsers() {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<List<AstroUser>> optionalLikedUsers = astroUserService.getLikedUsers(principalUser.get());

        if(optionalLikedUsers.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<AstroUser> likedUsers = optionalLikedUsers.get();

        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping("/matched-users")
    public ResponseEntity<List<AstroUser>> fetchMatchedUsers() {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<List<AstroUser>> optionalLikedUsers = astroUserService.getMatchedUsers(principalUser.get());

        if(optionalLikedUsers.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        List<AstroUser> likedUsers = optionalLikedUsers.get();

        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping("/filter-search")
    public ResponseEntity<?> fetchUsersWithFilters(@RequestBody FilterSearchRequest searchRequest) {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<AstroUser> users = astroUserService.findUsersWithFilters(searchRequest, principalUser.get());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeUserByUsernameOrEmail(@RequestBody SearchRequest likeRequest) {

        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

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

    @PostMapping("/block")
    public ResponseEntity<?> blockUserByUsernameOrEmail(@RequestBody SearchRequest blockRequest) {

        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (blockRequest != null) {
            user = astroUserService.getUserByUsername(blockRequest.getSearch());
            if (user.isEmpty()) {
                user = astroUserService.getUserByEmail(blockRequest.getSearch());
            }
        }

        if (user.isPresent()) {

            astroUserService.blockUser(user, principalUser);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/match")
    public ResponseEntity<?> matchUserByUsernameOrEmail(@RequestBody SearchRequest matchRequest) {

        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

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
}
