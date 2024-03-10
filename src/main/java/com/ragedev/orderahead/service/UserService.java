package com.ragedev.orderahead.service;

import com.ragedev.orderahead.dtos.AuthenticateRequest;
import com.ragedev.orderahead.dtos.AuthenticationResponse;
import com.ragedev.orderahead.exceptions.InvalidCredentialsException;
import com.ragedev.orderahead.exceptions.ResourceNotFoundException;
import com.ragedev.orderahead.models.User;
import com.ragedev.orderahead.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, JwtUtilService jwtUtilService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User saveUser(User user) {
        System.out.println(user);
        return userRepository.save(user);
    }
    // UserService.java
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
    // UserService.java

    public User updateUser(Integer id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());

        return userRepository.save(user);
    }

    public AuthenticationResponse register(User userDetails){
        if (userRepository.findByEmail(userDetails.getEmail()) != null) {
            throw new InvalidCredentialsException("Email already in use");
        }
        User user = User.builder().firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName()).email(userDetails.getEmail())
                .password(passwordEncoder.encode(userDetails.getPassword())).phoneNumber(userDetails.getPhoneNumber()).role(userDetails.getRole()).build();
        userRepository.save(user);
        var token = jwtUtilService.generateToken(user);

        return  AuthenticationResponse.builder().userId(user.getUserId()).token(token).firstName(user.getFirstName()).build();
    }
    public AuthenticationResponse authenticate(AuthenticateRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail());
            var token = jwtUtilService.generateToken(user);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);
            authenticationResponse.setUserId(user.getUserId());
            authenticationResponse.setFirstName(user.getFirstName());
            return authenticationResponse;
        }catch (AuthenticationException e){
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }
}

