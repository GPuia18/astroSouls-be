//package com.se.astro.user.service;
//
//import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
//import static javax.management.Query.times;
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static jdk.jfr.internal.jfc.model.Constraint.any;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;
//
//import com.se.astro.user.model.AstroUser;
//import com.se.astro.user.repository.AstroUserRepository;
//import com.se.astro.user.model.enums.Gender;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//public class AstroUserServiceTest {
//
//    @Mock
//    private MongoTemplate mongoTemplate;
//    @Mock
//    private AstroUserRepository astroUserRepository;
//    @Mock
//    private ZodiacService zodiacService;
//    @InjectMocks
//    private AstroUserService astroUserService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void getAllUsersTest() {
//        AstroUser user1 = new AstroUser(); // Create a mock AstroUser
//        AstroUser user2 = new AstroUser(); // Another mock AstroUser
//        when(astroUserRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
//
//        List<AstroUser> users = astroUserService.getAllUsers();
//
//        assertEquals(2, users.size());
//        verify(astroUserRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void getUserByIdTest() {
//        AstroUser user = new AstroUser();
//        user.setId("testId");
//        when(astroUserRepository.findById("testId")).thenReturn(Optional.of(user));
//
//        Optional<AstroUser> foundUser = astroUserService.getUserById("testId");
//
//        assertTrue(foundUser.isPresent());
//        assertEquals("testId", foundUser.get().getId());
//    }
//
//    @Test
//    public void getUserByUsernameTest() {
//        AstroUser user = new AstroUser();
//        user.setUsername("testUser");
//        when(astroUserRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//
//        Optional<AstroUser> foundUser = astroUserService.getUserByUsername("testUser");
//
//        assertTrue(foundUser.isPresent());
//        assertEquals("testUser", foundUser.get().getUsername());
//    }
//
//    @Test
//    public void likeUserTest() {
//        AstroUser principalUser = new AstroUser();
//        principalUser.setUsername("principalUser");
//        principalUser.setLikedUsers(Arrays.asList());
//
//        AstroUser userToLike = new AstroUser();
//        userToLike.setUsername("userToLike");
//
//        when(astroUserRepository.save(any(AstroUser.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        boolean result = astroUserService.likeUser(Optional.of(userToLike), Optional.of(principalUser));
//
//        assertTrue(result);
//        assertTrue(principalUser.getLikedUsers().contains("userToLike"));
//    }
//
//    @Test
//    public void blockUserTest() {
//        AstroUser principalUser = new AstroUser();
//        principalUser.setUsername("principalUser");
//        principalUser.setBlockedUsers(Arrays.asList());
//        AstroUser userToBlock = new AstroUser();
//        userToBlock.setUsername("userToBlock");
//
//        when(astroUserRepository.save(any(AstroUser.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        astroUserService.blockUser(Optional.of(userToBlock), Optional.of(principalUser));
//
//        assertTrue(principalUser.getBlockedUsers().contains("userToBlock"));
//        verify(astroUserRepository, times(2)).save(any(AstroUser.class)); // Once for each user
//    }
//
