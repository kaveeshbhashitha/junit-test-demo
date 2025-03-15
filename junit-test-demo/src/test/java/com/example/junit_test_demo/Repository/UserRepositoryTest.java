package com.example.junit_test_demo.Repository;

import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveAndFindUser() {
        User user = new User("Kamal", "Perera", "alice@example.com", "HR");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getUserId());

        assertTrue(foundUser.isPresent());
        assertEquals("Kamal", foundUser.get().getFirstName());
    }

    @Test
    public void shouldDeleteUser() {
        User user = new User("Kamal", "Perera", "alice@example.com", "HR");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getUserId());
        assertFalse(userRepository.findById(savedUser.getUserId()).isPresent());
    }
}
