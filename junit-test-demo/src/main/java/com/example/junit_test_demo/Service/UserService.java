package com.example.junit_test_demo.Service;
import com.example.junit_test_demo.Models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public User saveUser(User userDate);
    public List<User> getAllUsers();
    public Optional<User> getUserById(String userId);
    public User getUserByEmail(String userEmail);
    public List getUsersByDepartment(String department);
    public String deleteUser(String userId);
    public User updateUser(User userData, String userId);
}
