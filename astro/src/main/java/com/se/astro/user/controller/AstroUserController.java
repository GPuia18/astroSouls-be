package com.se.astro.user.controller;

import com.se.astro.product.model.Product;
import com.se.astro.product.repository.ProductRepository;
import com.se.astro.user.dto.*;
import com.se.astro.helper.UserPrincipalService;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.enums.AccountType;
import com.se.astro.user.service.AstroUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("@userPrincipalService.isCurrentUserNotBanned()")
public class AstroUserController {
    private final AstroUserService astroUserService;
    private final UserPrincipalService userPrincipalService;
    private final ProductRepository productRepository;

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
            if (user.isEmpty()) {
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

        if (optionalLikedUsers.isEmpty()) {
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

        if (optionalLikedUsers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<AstroUser> likedUsers = optionalLikedUsers.get();

        return ResponseEntity.ok(likedUsers);
    }

    @PostMapping("/filter-search")
    @PreAuthorize("hasAnyAuthority('ROLE_SILVER', 'ROLE_GOLD', 'ROLE_ADMIN')")
    public ResponseEntity<?> fetchUsersWithFilters(@RequestBody FilterSearchRequest searchRequest) {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Check if the subscription has expired
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration = principalUser.get().getPremiumExpiration();
        if (expiration != null && now.isAfter(expiration)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Subscription has expired.");
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

            boolean match = astroUserService.likeUser(user, principalUser);

            LikeUserResponse likeUserResponse = LikeUserResponse.builder().message((match) ? "Match" : "No Match").build();

            return ResponseEntity.ok(likeUserResponse);
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

            return ResponseEntity.ok(SuccessResponse.builder().message("Success").build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ban")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> banUserByUsernameOrEmail(@RequestBody SearchRequest banRequest) {

        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<AstroUser> user = Optional.empty();
        if (banRequest != null) {
            user = astroUserService.getUserByUsername(banRequest.getSearch());
            if (user.isEmpty()) {
                user = astroUserService.getUserByEmail(banRequest.getSearch());
            }
        }

        if (user.isPresent()) {

            if (user.get().getAccountType() != AccountType.ADMIN) {
                astroUserService.banUser(user);
            }

            return ResponseEntity.ok(SuccessResponse.builder().message("Success").build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/account-type")
    public ResponseEntity<?> setUserAccountType(@RequestBody AccountTypeRequest accountTypeRequest) {

        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty() || accountTypeRequest.getAccountType().equals("Admin")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            AccountType accountType = AccountType.valueOf(accountTypeRequest.getAccountType());

            Optional<Product> product = productRepository.findByName(accountType.name());

            if (product.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            astroUserService.changeUserAccountType(principalUser, accountType, product.get());

            return ResponseEntity.ok(SuccessResponse.builder().message("Success").build());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/compatible-users")
    public ResponseEntity<?> fetchCompatibleUsers() {
        Optional<AstroUser> principalUser = userPrincipalService.getPrincipalUser();

        if (principalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<UserCompatibility> users = astroUserService.findCompatibleUsers(principalUser.get());

        return ResponseEntity.ok(users);
    }

    @PutMapping("/edit-account")
    public ResponseEntity<AstroUser> editAccount(@RequestBody UpdateAccountRequest updateAccountRequest) {
        Optional<AstroUser> optionalPrincipalUser = userPrincipalService.getPrincipalUser();

        if (optionalPrincipalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AstroUser astroUser = astroUserService.editUser(updateAccountRequest, optionalPrincipalUser);

        return ResponseEntity.ok(astroUser);
    }
}
