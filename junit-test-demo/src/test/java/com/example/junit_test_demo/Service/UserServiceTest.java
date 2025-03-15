package com.example.junit_test_demo.Service;
import com.example.junit_test_demo.Impl.UserServiceImpl;
import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // Test: Save User
    @Test
    void shouldSaveUserSuccessfully() {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstName());
    }

    // Test: Get User by ID - Found
    @Test
    void shouldReturnUserByIdWhenFound() {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserById("1");
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    // Test: Get User by ID - Not Found
    @Test
    void shouldReturnEmptyOptionalWhenUserByIdNotFound() {
        when(userRepository.findById("2")).thenReturn(Optional.empty());
        Optional<User> foundUser = userService.getUserById("2");
        assertFalse(foundUser.isPresent());
    }

    // Test: Get All Users
    @Test
    void shouldReturnAllUsers() {
        List<User> users = Arrays.asList(
                new User("John", "Doe", "john@example.com", "STAT"),
                new User("Jane", "Smith", "jane@example.com", "HR")
        );

        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    // Test: Get User by Email - Found
    @Test
    void shouldReturnUserByEmailWhenFound() {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userRepository.findByUserEmail("john@example.com")).thenReturn(user);
        User foundUser = userService.getUserByEmail("john@example.com");
        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
    }

    // Test: Get User by Email - Not Found
    @Test
    void shouldReturnNullWhenUserByEmailNotFound() {
        when(userRepository.findByUserEmail("notfound@example.com")).thenReturn(null);
        User foundUser = userService.getUserByEmail("notfound@example.com");
        assertNull(foundUser);
    }

    // Test: Get Users by Department
    @Test
    void shouldReturnUsersByDepartment() {
        List<User> users = Arrays.asList(
                new User("John", "Doe", "john@example.com", "STAT"),
                new User("Jane", "Smith", "jane@example.com", "STAT")
        );

        when(userRepository.findByDepartment("STAT")).thenReturn(users);
        List<User> result = userService.getUsersByDepartment("STAT");
        assertEquals(2, result.size());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    // Test: Delete User - Valid ID
    @Test
    void shouldDeleteUser() {
        String userId = "1";
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUser(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void shouldNotDeleteUserWithInvalidId() {
        String userId = "";
        userService.deleteUser(userId);
        verify(userRepository, never()).deleteById(any());
    }

    // Test: Update User - Success
    @Test
    void shouldUpdateUserSuccessfully() {
        User existingUser = new User("John", "Doe", "john@example.com", "STAT");
        User updatedUser = new User("John", "Doe", "john.doe@example.com", "HR");
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        User result = userService.updateUser(updatedUser, "1");
        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUserEmail());
        assertEquals("HR", result.getDepartment());
    }

    // Test: Update User - User Not Found
    @Test
    void shouldReturnNullWhenUserToUpdateNotFound() {
        User updatedUser = new User("John", "Doe", "john.doe@example.com", "HR");
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        User result = userService.updateUser(updatedUser, "1");
        assertNull(result);
    }
}
