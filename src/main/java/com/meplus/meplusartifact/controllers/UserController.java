package com.meplus.meplusartifact.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.meplus.meplusartifact.models.User;
import com.meplus.meplusartifact.repos.UserRepo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
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

        return new HashMap<String, String>() {{
            put("status", "201");
            put("userID", savedUser.getId().toString());
            put("username", savedUser.getUsername());
            put("firstName", savedUser.getFirstName());
            put("lastName", savedUser.getLastName());

        }};
    }

    @GetMapping("/user")
    public Map<String, String> getUserById(@RequestParam Long id) {


        try {
            User foundUser = userRepo.getById(id);

            return new HashMap<String, String>() {{
                put("status", "201");
                put("userID", foundUser.getId().toString());
                put("username", foundUser.getUsername());
                put("firstName", foundUser.getFirstName());
                put("lastName", foundUser.getLastName());
            }};
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

        return new HashMap<String, String>(){{
            put("status", "201");
            put("userID", updatedUser.getId().toString());
            put("username", updatedUser.getUsername());
            put("firstName", updatedUser.getFirstName());
            put("lastName", updatedUser.getLastName());
        }};
    }


    @GetMapping("/user/login")
    public Map<String, String> loginUser(@RequestParam String username, @RequestParam String password) {

        try {
            User userInfo = userRepo.loginUser(username, password).get(0);
            return new HashMap<String, String>() {{
                put("status", "201");
                put("userId", userInfo.getId().toString());
                put("username", userInfo.getUsername());
                put("firstName", userInfo.getFirstName());
                put("lastName", userInfo.getLastName());
            }};
        }
        catch(IndexOutOfBoundsException e) {
            return new HashMap<String, String>() {{
                put("status", "406");
                put("error", "Not Acceptable");
                put("reason", "Username or Password was incorrect");
            }};
        }
    }

//    Temporary route to get all users currently in db, will be removed after testing stage
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }
}
