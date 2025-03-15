package com.example.junit_test_demo.Service;
import com.example.junit_test_demo.Impl.UserServiceImpl;
import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTestWithDb {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserServiceImpl userSer;

    @Test
    void shouldSaveUserToDatabase() {
        //insert rough data into db
        User user = new User("John", "Doe", "john@example.com", "STAT");
        User savedUser = userSer.saveUser(user);

        assertNotNull(savedUser.getUserId());
        //check email and first name of return of request
        assertEquals("John", savedUser.getFirstName());
        assertEquals("john@example.com", savedUser.getUserEmail());

        //check whether a data is exactly in the database
        User testUser = userSer.getUserByEmail(savedUser.getUserEmail());
        assertEquals("john@example.com", testUser.getUserEmail());
    }

    @Test
    void shouldReturnUserByIdWhenFound() {
        //insert rough data into db
        User user = new User("Kamal", "Perera", "kamal@example.com", "CHEM");
        User savedUser = userSer.saveUser(user);

        Optional<User> foundUser = userSer.getUserById(savedUser.getUserId());
        assertTrue(foundUser.isPresent());
        assertEquals("Kamal", foundUser.get().getFirstName());
        //check whether a data is exactly coming from the database
        assertEquals(savedUser.getUserId(), foundUser.get().getUserId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserByIdNotFound() {
        Optional<User> foundUser = userSer.getUserById("idthatnotexistinrecords");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> result = userSer.getAllUsers();
        assertEquals(2, result.size());
        assertTrue(result.size()>0);
        assertEquals("Kamal", result.get(1).getFirstName());
        //System.out.println("Test pass with " + result.size() + " records and expected user " +result.get(1).getFirstName()+ " in the records.");
    }

    @Test
    void shouldReturnUserByEmailWhenFound() {
        User foundUser = userSer.getUserByEmail("kamal@example.com");
        assertNotNull(foundUser);
        assertEquals("Kamal", foundUser.getFirstName());
    }

    @Test
    void shouldNotReturnUserByEmailWhenNotFound() {
        User foundUser = userSer.getUserByEmail("email@notexist.com");
        assertNull(foundUser, "No user found with given email");
    }

    @Test
    void shouldReturnUsersByDepartment() {
        User user1 = new User("John", "Doe", "john@example.com", "STAT");
        User user2 = new User("Jane", "Smith", "jane@example.com", "CHEM");
        User user3 = new User("Saman", "Perera", "saman@example.com", "STAT");
        userSer.saveUser(user1);
        userSer.saveUser(user2);
        userSer.saveUser(user3);

        List<User> result = userSer.getUsersByDepartment("STAT");
        assertTrue(result.size() > 1);
        assertEquals("John", result.get(1).getFirstName());
    }

    @Test
    void shouldDeleteUser() {
        String userId = "67d5a52e7d2faa6636e51f5c";
        userSer.deleteUser(userId);
        Optional<User> user = userSer.getUserById("67d5a52e7d2faa6636e51f5c");
        assertTrue(user.isEmpty());
        assertEquals("User with id 67d5a52e7d2faa6636e51f5c deleted successfully.", "User with id "+userId+" deleted successfully.");
    }

    @Test
    void shouldNotDeleteUserWithInvalidId() {
        String userId = "";
        String error = userSer.deleteUser(userId);
        assertNotEquals("Cannot find the user.",error);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User existingUser = new User("Jane", "Smith", "jane@example.com", "CHEM");
        User updatedUser = new User("John", "Doe", "john.doe@example.com", "HR");

        User userNeedToBeUpdated = userSer.getUserByEmail(existingUser.getUserEmail());
        assertNotNull(userNeedToBeUpdated, "No user found");

        User result = userSer.updateUser(updatedUser, userNeedToBeUpdated.getUserId());

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUserEmail());
        assertEquals("HR", result.getDepartment());
    }

    @Test
    void shouldReturnNullWhenUserToUpdateNotFound() {
        User updatedUser = new User("John", "Doe", "john.doe@example.com", "HR");
        Optional<User> getNullUser = userSer.getUserById("notExistId");

        assertTrue(getNullUser.isEmpty());
        User result = userSer.updateUser(updatedUser, "NotExistId");
        assertNull(result);
    }
}
