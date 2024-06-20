package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service // Similar to @component annotation this also creates bean but this informs that it is a service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(User user) {
        userRepository.save(user);
    }
    /*
        Use saveNewUser only for a new user not for updating journals of the user
        It will again encode the existing password causing 401 unauthorized issue
     */
    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("user"));
        userRepository.save(user);
    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("user"));
        userRepository.save(user);
    }

    public List<User> getAllUserEntries() {
        return userRepository.findAll();
    }

    public Optional<User> findByID(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteByID(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void addAdminRole(String username) {
        try {
            User user = findByUsername(username);

            user.getRoles().add("ADMIN");
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error caused in updating to admin role", e);
        }
    }
}
