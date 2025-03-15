package com.example.junit_test_demo.Repos;
import com.example.junit_test_demo.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByDepartment(String department);
    public User findByUserEmail(String userEmail);
}
