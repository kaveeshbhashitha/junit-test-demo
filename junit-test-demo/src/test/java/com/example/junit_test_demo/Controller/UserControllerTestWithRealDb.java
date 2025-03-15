package com.example.junit_test_demo.Controller;
import com.example.junit_test_demo.Impl.UserServiceImpl;
import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTestWithRealDb {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserServiceImpl userService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() {
        User user = new User("John", "Doe", "john@example.com", "STAT");

        webTestClient.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.userEmail").isEqualTo("john@example.com");

        List<User> users = userService.getAllUsers();
        assertThat(users).hasSize(5);
        assertTrue(users.size() > 1);
    }

    @Test
    void shouldGetUserById() {
        User savedUser = userService.saveUser(new User("John", "Doe", "john@example.com", "STAT"));

        webTestClient.get().uri("/api/users/" + savedUser.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.userEmail").isEqualTo("john@example.com");
    }

    @Test
    void shouldGetAllUsers() {
        userRepository.saveAll(List.of(
                new User("John", "Doe", "john@example.com", "STAT"),
                new User("Jane", "Doe", "jane@example.com", "IT")
        ));

        webTestClient.get().uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$[0].firstName").isEqualTo("John")
                .jsonPath("$[1].firstName").isEqualTo("Jane");
    }

    @Test
    void shouldDeleteUser() {
        User savedUser = userService.saveUser(new User("John", "Doe", "john@example.com", "STAT"));
        User newlyAddedUser = userService.getUserByEmail(savedUser.getUserEmail());
        webTestClient.delete().uri("/api/users/" + savedUser.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("User deleted successfully.");

        assertThat(userRepository.findById(savedUser.getUserId())).isEmpty();
    }

    @Test
    void shouldUpdateUser() {
        User savedUser = userRepository.save(new User("John", "Doe", "john@example.com", "STAT"));

        User updatedUser = new User("John", "Smith", "johnsmith@example.com", "IT");

        webTestClient.put().uri("/api/users/" + savedUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.lastName").isEqualTo("Smith");
    }
}
