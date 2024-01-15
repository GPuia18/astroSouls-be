package com.se.astro.user.service;

import com.se.astro.product.model.Product;
import com.se.astro.user.dto.UpdateAccountRequest;
import com.se.astro.user.dto.UserCompatibility;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.dto.FilterSearchRequest;
import com.se.astro.user.model.ZodiacCompatibility;
import com.se.astro.user.model.enums.AccountType;
import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Language;
import com.se.astro.user.model.enums.Tag;
import com.se.astro.user.repository.AstroUserRepository;
import com.se.astro.user.repository.ZodiacCompatibilityRepository;
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
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AstroUserService {

    private final MongoTemplate mongoTemplate;
    private final AstroUserRepository astroUserRepository;
    private final ZodiacService zodiacService;

    public List<AstroUser> getAllUsers() {
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

    public Optional<List<AstroUser>> getLikedUsers(AstroUser user) {
        return astroUserRepository.findAllByUsername(user.getLikedUsers());
    }

    public Optional<List<AstroUser>> getMatchedUsers(AstroUser user) {
        return astroUserRepository.findAllByUsername(user.getMatchedUsers());
    }

    public List<UserCompatibility> findCompatibleUsers(AstroUser principalUser) {
        String zodiacSign = principalUser.getZodiacSign();
        List<Language> languages = principalUser.getLanguage();
        List<Gender> genderList = principalUser.getSearchingFor();

        Optional<List<ZodiacCompatibility>> optionalZodiacCompatibilities = zodiacService.findCompatibilitiesByZodiacName(zodiacSign);

        if (optionalZodiacCompatibilities.isEmpty()) {
            return List.of();
        }

        List<ZodiacCompatibility> zodiacCompatibilities = optionalZodiacCompatibilities.get();

        List<String> compatibleZodiacs = zodiacCompatibilities.stream()
                .map(zc -> zodiacSign.equals(zc.getZodiacName1()) ? zc.getZodiacName2() : zc.getZodiacName1())
                .collect(Collectors.toList());

        List<AstroUser> users = astroUserRepository.findAllByLanguageAndGender(
                languages.stream().map(Language::name).collect(Collectors.toList()),
                genderList.stream().map(Gender::name).collect(Collectors.toList()));

        List<AstroUser> astroUserList = users.stream()
                .filter(user -> !user.getUsername().equals(principalUser.getUsername()))
                .filter(user -> compatibleZodiacs.contains(user.getZodiacSign()))
                .filter(user -> user.getSearchingFor() != null && user.getSearchingFor().contains(principalUser.getGender()))
                .filter(user -> principalUser.getLikedUsers() != null && !principalUser.getLikedUsers().contains(user.getUsername()))
                .sorted((u1, u2) -> {
                    int score1 = getCompatibilityScore(u1.getZodiacSign(), zodiacCompatibilities);
                    int score2 = getCompatibilityScore(u2.getZodiacSign(), zodiacCompatibilities);
                    return Integer.compare(score2, score1);
                })
                .collect(Collectors.toList());

        return astroUserList.stream()
                .map(user -> {
                    int score = getCompatibilityScore(user.getZodiacSign(), zodiacCompatibilities);
                    return new UserCompatibility(user, score);
                })
                .collect(Collectors.toList());
    }

    private int getCompatibilityScore(String zodiacSign, List<ZodiacCompatibility> compatibilities) {
        return compatibilities.stream()
                .filter(zc -> zc.getZodiacName1().equals(zodiacSign) || zc.getZodiacName2().equals(zodiacSign))
                .findFirst()
                .map(ZodiacCompatibility::getCompatibility)
                .orElse(0);
    }

    public List<AstroUser> findUsersWithFilters(FilterSearchRequest searchRequest, AstroUser principalUser) {
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

        List<String> likedUsers = principalUser.getLikedUsers();
        if (!likedUsers.isEmpty()) {
            likedUsers.add(principalUser.getUsername());
            query.addCriteria(Criteria.where("username").nin(likedUsers));
        }

        List<AstroUser> users = mongoTemplate.find(query, AstroUser.class);
        return users;
    }

    public void likeUser(Optional<AstroUser> user, Optional<AstroUser> principalUser) {
        AstroUser userToLike = user.get();
        AstroUser userThatLikes = principalUser.get();

        if (userThatLikes.getLikedUsers().contains(userToLike.getUsername())) {
            return;
        }

        userThatLikes.likeUser(userToLike);

        if (userToLike.getLikedUsers().contains(userThatLikes.getUsername())) {
            userThatLikes.createMatch(userToLike);
            userToLike.createMatch(userThatLikes);

            astroUserRepository.save(userToLike);
        }

        astroUserRepository.save(userThatLikes);
    }

    public void blockUser(Optional<AstroUser> user, Optional<AstroUser> principalUser) {
        AstroUser userToBlock = user.get();
        AstroUser userThatBlocks = principalUser.get();

        if (userThatBlocks.getBlockedUsers().contains(userToBlock.getUsername())) {
            return;
        }

        userThatBlocks.blockUser(userToBlock);

        astroUserRepository.save(userThatBlocks);
    }

    public void banUser(Optional<AstroUser> user) {
        AstroUser userToBlock = user.get();

        userToBlock.setBanned(true);

        astroUserRepository.save(userToBlock);
    }

    public void changeUserAccountType(Optional<AstroUser> user, AccountType accountType, Product product) {
        AstroUser userToChange = user.get();

        userToChange.setAccountType(accountType);
        userToChange.setPremiumExpiration(LocalDateTime.now().plusMonths(product.getLength()));

        astroUserRepository.save(userToChange);
    }

    public void editUser(UpdateAccountRequest updateAccountRequest, Optional<AstroUser> optionalPrincipalUser) {
        AstroUser principalUser = optionalPrincipalUser.get();

        if (updateAccountRequest.getZodiacSign() != null) {
            principalUser.setZodiacSign(updateAccountRequest.getZodiacSign());
        }
        if (updateAccountRequest.getGender() != null) {
            principalUser.setGender(updateAccountRequest.getGender());
        }
        if (updateAccountRequest.getSearchingFor() != null) {
            principalUser.setSearchingFor(updateAccountRequest.getSearchingFor());
        }
        if (updateAccountRequest.getHeight() != null) {
            principalUser.setHeight(updateAccountRequest.getHeight());
        }
        if (updateAccountRequest.getNationality() != null) {
            principalUser.setNationality(updateAccountRequest.getNationality());
        }
        if (updateAccountRequest.getLanguage() != null) {
            principalUser.setLanguage(updateAccountRequest.getLanguage());
        }
        if (updateAccountRequest.getHeader() != null) {
            principalUser.setHeader(updateAccountRequest.getHeader());
        }
        if (updateAccountRequest.getDescription() != null) {
            principalUser.setDescription(updateAccountRequest.getDescription());
        }
        if (updateAccountRequest.getAgeRangeMin() != null) {
            principalUser.setAgeRangeMin(updateAccountRequest.getAgeRangeMin());
        }
        if (updateAccountRequest.getAgeRangeMax() != null) {
            principalUser.setAgeRangeMax(updateAccountRequest.getAgeRangeMax());
        }
        if (updateAccountRequest.getTags() != null) {
            principalUser.setTags(updateAccountRequest.getTags());
        }
        if (updateAccountRequest.getImages() != null) {
            principalUser.setImages(updateAccountRequest.getImages());
        }

        AstroUser updatedUser = astroUserRepository.save(principalUser);
    }
}
