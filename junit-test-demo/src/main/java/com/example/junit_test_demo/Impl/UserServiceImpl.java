package com.example.junit_test_demo.Impl;
import com.example.junit_test_demo.Models.User;
import com.example.junit_test_demo.Repos.UserRepository;
import com.example.junit_test_demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public User saveUser(User userDate) {
        return userRepo.save(userDate);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepo.findByUserEmail(userEmail);
    }

    @Override
    public List getUsersByDepartment(String department) {
        return userRepo.findByDepartment(department);
    }

    @Override
    public void deleteUser(String userId) {
        if (userId != null || userId != ""){
            userRepo.deleteById(userId);
            System.out.println("User with id "+userId+" deleted successfully.");
        }else{
            System.out.println("Cannot find the user.");
        }
    }
    @Override
    public User updateUser(User userData, String userId) {
        Optional<User> optionalExistingUser = userRepo.findById(userId);

        if (!optionalExistingUser.isPresent()) {
            System.out.println("User not found with "+ userData.getUserId());;
        }else{
            User existingUser = optionalExistingUser.get();
            existingUser.setUserEmail(userData.getUserEmail());
            existingUser.setFirstName(userData.getFirstName());
            existingUser.setLastName(userData.getLastName());
            existingUser.setDepartment(userData.getDepartment());
            return userRepo.save(existingUser);
        }
        return null;
    }
}
