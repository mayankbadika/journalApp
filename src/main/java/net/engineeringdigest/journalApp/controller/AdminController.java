package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUserEntries();

        if(users != null && !users.isEmpty()) {
            return  new ResponseEntity<>(users, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-admin")
    public ResponseEntity<?> addAdminRole(@RequestBody User user) {
        userService.saveAdmin(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-to-admin")
    public ResponseEntity<?> updateToAdmin(@RequestBody User user) {
        try {
            userService.addAdminRole(user.getUsername());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
