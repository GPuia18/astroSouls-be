package test;

import com.se.astro.user.model.AstroUser;
import com.se.astro.user.repository.AstroUserRepository;
import com.se.astro.user.service.AstroUserService;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AstroUserServiceTest {

    @Mock
    private AstroUserRepository astroUserRepository;

    @InjectMocks
    private AstroUserService astroUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
}
