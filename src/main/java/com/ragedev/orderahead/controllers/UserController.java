package com.ragedev.orderahead.controllers;
// UserController.java
import com.ragedev.orderahead.dtos.AuthenticateRequest;
import com.ragedev.orderahead.dtos.AuthenticationResponse;
import com.ragedev.orderahead.dtos.RegisterRestaurantRequest;
import com.ragedev.orderahead.exceptions.InvalidCredentialsException;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.Restaurant;
import com.ragedev.orderahead.models.User;
import com.ragedev.orderahead.service.RestaurantService;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ragedev.orderahead.service.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final RestaurantService restaurantService;
    @Autowired
    public UserController(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidCredentials(@NotNull InvalidCredentialsException ex) {
        return ex.getMessage();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @Transactional
    @PostMapping("/manager/register")
    public ResponseEntity<?> registerManager(@RequestBody RegisterRestaurantRequest request) {
        AuthenticationResponse createdUser = (userService.register(request.getUser()));
        Restaurant createdRestaurant = restaurantService.saveRestaurant(request.getRestaurant(), createdUser.getUserId());
        Map <String, Object> response = new HashMap<>();
        response.put("authenticationResponse" , createdUser);
        response.put("restaurantId" , createdRestaurant.getRestaurantId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manager/login")
    public ResponseEntity<?> loginManager(@RequestBody AuthenticateRequest authenticateRequest) {
        System.out.println(authenticateRequest);
        try {
            AuthenticationResponse authenticationResponse = (userService.authenticate(authenticateRequest));
            Integer restaurantId = restaurantService.getRestaurantByManagerId(authenticationResponse.getUserId()).getRestaurantId();
            Map <String,Object> loginResponse = new HashMap<String,Object>();
            loginResponse.put("authenticationResponse", authenticationResponse);
            loginResponse.put("restaurantId" , restaurantId);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticateRequest authenticateRequest) {
        System.out.println(authenticateRequest);
        try {
            return ResponseEntity.ok(userService.authenticate(authenticateRequest));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(@NotNull ResourceNotFoundException ex){
        return ex.getMessage();
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping("/users/register")
    public ResponseEntity<Integer> createUser(@RequestBody User user) {
        System.out.println(user);
        User savedUser = userService.saveUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getUserId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }
}