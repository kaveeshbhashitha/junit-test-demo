package com.example.junit_test_demo.Controller;

import com.example.junit_test_demo.Controllers.UserController;
import com.example.junit_test_demo.Impl.UserServiceImpl;
import com.example.junit_test_demo.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class) // Use latest Mockito setup
@WebMvcTest(UserController.class)
public class UserControllerMockTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;  // Use @Mock instead of @MockBean

    @InjectMocks
    private UserController userController; // Inject controller with mocked service

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"userEmail\":\"john@example.com\", \"department\":\"STAT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userService.getUserById(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("john@example.com"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        User user1 = new User("John", "Doe", "john@example.com", "STAT");
        User user2 = new User("Jane", "Doe", "jane@example.com", "IT");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    void shouldGetUserByEmail() throws Exception {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/users/email/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("john@example.com"));
    }

    @Test
    void shouldGetUsersByDepartment() throws Exception {
        User user = new User("John", "Doe", "john@example.com", "STAT");
        when(userService.getUsersByDepartment(anyString())).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users/department/STAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("STAT"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User updatedUser = new User("John", "Smith", "johnsmith@example.com", "IT");
        when(userService.updateUser(any(User.class), anyString())).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"userEmail\":\"john@example.com\", \"department\":\"STAT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }
}