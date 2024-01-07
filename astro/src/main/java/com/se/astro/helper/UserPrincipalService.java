package com.se.astro.helper;

import com.se.astro.user.model.AstroUser;
import com.se.astro.user.service.AstroUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserPrincipalService {

    private final AstroUserService astroUserService;

    public UserPrincipalService(AstroUserService astroUserService) {
        this.astroUserService = astroUserService;
    }

    public Optional<AstroUser> getPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentUsername = userDetails.getUsername();
        }

        return astroUserService.getUserByUsername(currentUsername);
    }
}
