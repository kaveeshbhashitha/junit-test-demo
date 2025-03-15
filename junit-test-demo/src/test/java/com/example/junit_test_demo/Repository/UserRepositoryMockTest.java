package com.example.junit_test_demo.Repository;
import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import com.example.junit_test_demo.Service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryMockTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // If you have a service layer

    @Test
    public void shouldSaveAndFindUser() {
        User user = new User("Kamal", "Perera", "alice@example.com", "HR");
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> foundUser = Optional.of(user);
        when(userRepository.findById(user.getUserId())).thenReturn(foundUser);

        assertTrue(foundUser.isPresent());
        assertEquals("Kamal", foundUser.get().getFirstName());
    }
}
