package com.se.astro.authentication.service;

import com.se.astro.authentication.model.AuthenticationRequest;
import com.se.astro.authentication.model.AuthenticationResponse;
import com.se.astro.authentication.model.RegisterRequest;
import com.se.astro.config.JwtService;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.enums.AccountType;
import com.se.astro.user.repository.AstroUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AstroUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<AstroUser> userInRepo = repository.findByUsername(request.getUsername());

        if(userInRepo.isPresent()){
            return null;
        }

        userInRepo = repository.findByEmail(request.getEmail());

        if(userInRepo.isPresent()){
            return null;
        }

        var user = AstroUser.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountType(AccountType.REGULAR) // Assuming this is a fixed value for all users
                .birthday(request.getBirthday())
                .zodiacSign(request.getZodiacSign())
                .gender(request.getGender())
                .searchingFor(request.getSearchingFor())
                .height(request.getHeight())
                .nationality(request.getNationality())
                .language(request.getLanguage())
                .header(request.getHeader())
                .description(request.getDescription())
                .ageRangeMin(request.getAgeRangeMin())
                .ageRangeMax(request.getAgeRangeMax())
                .tags(request.getTags())
                .likedUsers(new ArrayList<>())
                .matchedUsers(new ArrayList<>())
                .blockedUsers(new ArrayList<>())
                .messages(new ArrayList<>())
                .groups(new ArrayList<>())
                .banned(false)
                .build();

        repository.save(user);

        return createAuthenticationResponse(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getUsername())
                .or(() -> repository.findByUsername(request.getUsername()))
                .orElseThrow();

        return createAuthenticationResponse(user);
    }

    private AuthenticationResponse createAuthenticationResponse(UserDetails user){
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
