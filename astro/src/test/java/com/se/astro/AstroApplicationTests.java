package com.se.astro;

import com.se.astro.user.dto.UpdateAccountRequest;
import com.se.astro.user.model.AstroUser;
import com.se.astro.user.model.enums.Gender;
import com.se.astro.user.model.enums.Language;
import com.se.astro.user.model.enums.Nationality;
import com.se.astro.user.model.enums.Tag;
import com.se.astro.user.repository.AstroUserRepository;
import com.se.astro.user.service.AstroUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertFalse;
import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AstroApplicationTests {

    @Mock
    private AstroUserRepository astroUserRepository;

    @InjectMocks
    private AstroUserService astroUserService;

    @Test
    void getAllUsersTest() {
        List<AstroUser> expectedUsers = List.of(new AstroUser(), new AstroUser());
        when(astroUserRepository.findAll()).thenReturn(expectedUsers);

        List<AstroUser> actualUsers = astroUserService.getAllUsers();

        verify(astroUserRepository, times(1)).findAll();
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUserByIdTest() {
        String id = "testId";
        Optional<AstroUser> expectedUser = Optional.of(new AstroUser());
        when(astroUserRepository.findById(id)).thenReturn(expectedUser);

        Optional<AstroUser> actualUser = astroUserService.getUserById(id);

        verify(astroUserRepository, times(1)).findById(id);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByUsernameTest() {
        String username = "testUser";
        Optional<AstroUser> expectedUser = Optional.of(new AstroUser());
        when(astroUserRepository.findByUsername(username)).thenReturn(expectedUser);

        Optional<AstroUser> actualUser = astroUserService.getUserByUsername(username);

        verify(astroUserRepository, times(1)).findByUsername(username);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByEmailTest() {
        String email = "test@example.com";
        Optional<AstroUser> expectedUser = Optional.of(new AstroUser());
        when(astroUserRepository.findByEmail(email)).thenReturn(expectedUser);

        Optional<AstroUser> actualUser = astroUserService.getUserByEmail(email);

        verify(astroUserRepository, times(1)).findByEmail(email);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void likeUserTest() {
        String usernameToLike = "userToLike";
        String usernameThatLikes = "userThatLikes";

        AstroUser userToLike = new AstroUser();
        userToLike.setUsername(usernameToLike);
        userToLike.setLikedUsers(new ArrayList<>());
        userToLike.setMatchedUsers(new ArrayList<>()); // Initialize matchedUsers

        AstroUser userThatLikes = new AstroUser();
        userThatLikes.setUsername(usernameThatLikes);
        userThatLikes.setLikedUsers(new ArrayList<>());
        userThatLikes.setMatchedUsers(new ArrayList<>()); // Initialize matchedUsers

        // Mock userToLike already liked userThatLikes
        userToLike.getLikedUsers().add(usernameThatLikes);

        when(astroUserRepository.findById(usernameToLike)).thenReturn(Optional.of(userToLike));
        when(astroUserRepository.findById(usernameThatLikes)).thenReturn(Optional.of(userThatLikes));

        boolean result = astroUserService.likeUser(Optional.of(userToLike), Optional.of(userThatLikes));

        assertTrue(result);
        assertTrue(userThatLikes.getLikedUsers().contains(usernameToLike));
        assertTrue(userThatLikes.getMatchedUsers().contains(usernameToLike));
        assertTrue(userToLike.getMatchedUsers().contains(usernameThatLikes));
        verify(astroUserRepository, times(1)).save(userThatLikes);
        verify(astroUserRepository, times(1)).save(userToLike);
    }


    @Test
    void blockUserTest() {
        String usernameToBlock = "userToBlock";
        String usernameThatBlocks = "userThatBlocks";

        AstroUser userToBlock = new AstroUser();
        userToBlock.setUsername(usernameToBlock);
        userToBlock.setMatchedUsers(new ArrayList<>());

        AstroUser userThatBlocks = new AstroUser();
        userThatBlocks.setUsername(usernameThatBlocks);
        userThatBlocks.setBlockedUsers(new ArrayList<>());
        userThatBlocks.setMatchedUsers(new ArrayList<>(List.of(usernameToBlock)));

        when(astroUserRepository.findById(usernameToBlock)).thenReturn(Optional.of(userToBlock));
        when(astroUserRepository.findById(usernameThatBlocks)).thenReturn(Optional.of(userThatBlocks));

        astroUserService.blockUser(Optional.of(userToBlock), Optional.of(userThatBlocks));

        assertTrue(userThatBlocks.getBlockedUsers().contains(usernameToBlock));
        assertFalse(userThatBlocks.getMatchedUsers().contains(usernameToBlock));
        verify(astroUserRepository, times(1)).save(userThatBlocks);
        verify(astroUserRepository, times(1)).save(userToBlock);
    }


    @Test
    void editUserTest() {
        // Creating and setting properties on updateRequest
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setZodiacSign("Aries");
        updateRequest.setGender(Gender.MALE);
        updateRequest.setSearchingFor(List.of(Gender.FEMALE, Gender.OTHER));
        updateRequest.setHeight(175.5);
        updateRequest.setNationality(Nationality.ALBANIA);
        updateRequest.setLanguage(List.of(Language.ENGLISH, Language.SPANISH, Language.GERMAN));
        updateRequest.setHeader("About Me");
        updateRequest.setDescription("I am a software developer interested in astrology.");
        updateRequest.setAgeRangeMin(25);
        updateRequest.setAgeRangeMax(35);
        updateRequest.setTags(List.of(Tag.FOODIE, Tag.TRAVELER));
        updateRequest.setImages(List.of("image1.jpg", "image2.jpg"));

        // Creating principalUser and setting the username
        String principalUsername = "principalUser";
        AstroUser principalUser = new AstroUser();
        principalUser.setUsername(principalUsername);

        // Mocking repository call
        when(astroUserRepository.findById(principalUsername)).thenReturn(Optional.of(principalUser));

        // Calling the method under test
        AstroUser updatedUser = astroUserService.editUser(updateRequest, Optional.of(principalUser));

        // Assertions to verify the update
        assertNotNull(updatedUser);
        assertEquals("Aries", updatedUser.getZodiacSign());
        assertEquals(Gender.MALE, updatedUser.getGender());
        assertEquals(List.of(Gender.FEMALE, Gender.OTHER), updatedUser.getSearchingFor());
        assertEquals(175.5, updatedUser.getHeight());
        assertEquals(Nationality.ALBANIA, updatedUser.getNationality());
        assertEquals(List.of(Language.ENGLISH, Language.SPANISH, Language.GERMAN), updatedUser.getLanguage());
        assertEquals("About Me", updatedUser.getHeader());
        assertEquals("I am a software developer interested in astrology.", updatedUser.getDescription());
        assertEquals(25, updatedUser.getAgeRangeMin());
        assertEquals(35, updatedUser.getAgeRangeMax());
        assertEquals(List.of(Tag.FOODIE, Tag.TRAVELER), updatedUser.getTags());
        assertEquals(List.of("image1.jpg", "image2.jpg"), updatedUser.getImages());

        verify(astroUserRepository, times(1)).save(principalUser);
    }
}