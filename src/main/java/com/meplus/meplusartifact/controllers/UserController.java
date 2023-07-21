package com.meplus.meplusartifact.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.meplus.meplusartifact.models.User;
import com.meplus.meplusartifact.repos.UserRepo;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    @Autowired
    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @PostMapping("/user")
    public Map<String, String> createUser(@RequestBody User user) {

        if (userRepo.existsByUsername(user.getUsername())) {
            return new HashMap<String, String>() {{
                put("status", "406");
                put("error", "Not Acceptable");
                put("reason", String.format("Username: %s, already exists", user.getUsername()));
            }};
        }
        User savedUser = userRepo.save(user);
        return createResponse(savedUser, true);
    }


    @GetMapping("/user")
    public Map<String, String> getUserById(@RequestParam Long id) {

        try {
            User foundUser = userRepo.getById(id);

            return createResponse(foundUser, true);
        }
        catch(EntityNotFoundException e) {
            return new HashMap<String, String>() {{
                put("status", "406");
                put("error", "Not Acceptable");
                put("reason", String.format("UserID: %s, doesn't exists", id));
            }};
        }
    }


    @PostMapping("user/update")
    public Map<String, String> updateUserInfo(@RequestParam Long id, @RequestParam String firstName, @RequestParam String lastName) {

        userRepo.updateUserInfo(id, firstName, lastName);
        User updatedUser = userRepo.getById(id);
        return createResponse(updatedUser, true);
    }


    @GetMapping("/user/login")
    public Map<String, String> loginUser(@RequestParam String username, @RequestParam String password) {

        try {
            User userInfo = userRepo.loginUser(username, password).get(0);
            return createResponse(userInfo, true);
        }
        catch(IndexOutOfBoundsException e) {
            return new HashMap<String, String>() {{
                put("status", "406");
                put("error", "Not Acceptable");
                put("reason", "Username or Password was incorrect");
            }};
        }
    }


    private Map<String, String> createResponse(User user, Boolean status) {
        Map<String, String> response = new HashMap<String, String>() {{

            put("userId", user.getId().toString());
            put("username", user.getUsername());
            put("firstName", user.getFirstName());
            put("lastName", user.getLastName());
        }};

        if (status == true) {
            response.put("status", "201");
        }
        return response;
    }

//    Temporary route to get all users currently in db, will be removed after testing stage
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }
}
