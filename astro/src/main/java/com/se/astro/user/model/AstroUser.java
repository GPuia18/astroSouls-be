package com.se.astro.user.model;

import com.se.astro.user.model.enums.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Document
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AstroUser implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    @Indexed(unique = true)
    private String email;
    private LocalDateTime birthday;
    private String zodiacSign;
    private Gender gender;
    private List<Gender> searchingFor;
    private double height;
    private Nationality nationality;
    private List<Language> language;
    private String header;
    private String description;
    private int ageRangeMin;
    private int ageRangeMax;
    private List<Tag> tags;
    private List<String> likedUsers;
    private List<String> matchedUsers;
    private AccountType accountType;
    private boolean banned;
    private List<String> blockedUsers;
    private List<Message> messages;
    private List<Group> groups;


    public AstroUser(String username, String password, String email, LocalDateTime birthday,
                     String zodiacSign, Gender gender, List<Gender> searchingFor, double height,
                     Nationality nationality, List<Language> language, String header, String description,
                     int ageRangeMin, int ageRangeMax, List<Tag> tags, AccountType accountType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
        this.zodiacSign = zodiacSign;
        this.gender = gender;
        this.searchingFor = searchingFor;
        this.height = height;
        this.nationality = nationality;
        this.language = language;
        this.header = header;
        this.description = description;
        this.ageRangeMin = ageRangeMin;
        this.ageRangeMax = ageRangeMax;
        this.tags = tags;
        this.accountType = accountType;

        this.likedUsers = new ArrayList<String>();
        this.matchedUsers = new ArrayList<String>();
        this.blockedUsers = new ArrayList<String>();
        this.messages = new ArrayList<Message>();
        this.groups = new ArrayList<Group>();
        this.banned = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(accountType.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
