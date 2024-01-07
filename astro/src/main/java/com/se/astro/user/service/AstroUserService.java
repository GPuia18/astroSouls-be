package com.se.astro.user.service;

import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.UserSearchRequest;
import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Tag;
import com.se.astro.user.repository.AstroUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AstroUserService {

    private final MongoTemplate mongoTemplate;
    private final AstroUserRepository astroUserRepository;



    public List<AstroUser> getAllUsers(){
        return astroUserRepository.findAll();
    }

    public Optional<AstroUser> getUserById(String id) {
        return astroUserRepository.findById(id);
    }

    public Optional<AstroUser> getUserByUsername(String username) {
        return astroUserRepository.findByUsername(username);
    }

    public Optional<AstroUser> getUserByEmail(String email) {
        return astroUserRepository.findByEmail(email);
    }

    public List<AstroUser> findUsersWithFilters(UserSearchRequest searchRequest) {
        Query query = new Query();

        if (searchRequest.getMinHeight() != null && searchRequest.getMaxHeight() != null) {
            query.addCriteria(Criteria.where("height").gte(searchRequest.getMinHeight())
                    .lte(searchRequest.getMaxHeight()));
        }
        if (searchRequest.getMinAge() != null && searchRequest.getMaxAge() != null) {
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime minBirthday = now.minusYears(searchRequest.getMinAge());

            LocalDateTime maxBirthday = now.minusYears(searchRequest.getMaxAge()).minusYears(1);

            Date maxBirthdayDate = Date.from(minBirthday.atZone(ZoneId.systemDefault()).toInstant());
            Date minBirthdayDate = Date.from(maxBirthday.atZone(ZoneId.systemDefault()).toInstant());

            query.addCriteria(Criteria.where("birthday").gte(minBirthdayDate).lte(maxBirthdayDate));
        }

        if (searchRequest.getTags() != null && !searchRequest.getTags().isEmpty()) {
            List<Tag> tagList = searchRequest.getTags();
            List<String> tagStringList = tagList.stream().map(Enum::name).toList();
            query.addCriteria(Criteria.where("tags").in(tagStringList));
        }
        if (searchRequest.getSearchingFor() != null && !searchRequest.getSearchingFor().isEmpty()) {
            List<Gender> genderList = searchRequest.getSearchingFor();
            List<String> genderStringList = genderList.stream().map(Enum::name).toList();
            query.addCriteria(Criteria.where("lookingFor").in(genderStringList));
        }
        if (searchRequest.getZodiacSign() != null) {
            query.addCriteria(Criteria.where("zodiacSign").is(searchRequest.getZodiacSign()));
        }

        List<AstroUser> users = mongoTemplate.find(query, AstroUser.class);
        return users;
    }

    public void likeUser(Optional<AstroUser> user, Optional<AstroUser> principalUser) {
        AstroUser userToLike = user.get();
        AstroUser userThatLikes = principalUser.get();

        userThatLikes.likeUser(userToLike);

        astroUserRepository.save(userThatLikes);
    }
}
