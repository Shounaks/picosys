package com.example.picosys.service;

import com.example.picosys.entity.User;
import com.example.picosys.entity.security.AuthenticationResponse;
import com.example.picosys.entity.security.LoginRequest;
import com.example.picosys.entity.security.RegisterRequest;
import com.example.picosys.exceptions.PartnerCompServiceException;
import com.example.picosys.repository.UserRepository;
import com.example.picosys.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Optional<User> retrieveUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> retrieveUserByEmail(String email) {
        return userRepository.findByEmailId(email);
    }

    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        userRepository.findByEmailId(registerRequest.getEmailId()).ifPresent(x -> {
            throw new PartnerCompServiceException("User Registration Error: Email Already Registered");
        });
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .emailId(registerRequest.getEmailId())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .location(registerRequest.getLocation())
                .jobTitle(registerRequest.getJobTitle())
                .department(registerRequest.getDepartment())
                .role(registerRequest.getRole())
                .build();
        User newUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user.getEmailId());
        return generateAuthenticationResponse(newUser, jwtToken);
    }

    public AuthenticationResponse loginUser(LoginRequest registerRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getEmailId(), registerRequest.getPassword()));
        var user = userRepository.findByEmailId(registerRequest.getEmailId()).orElseThrow(() -> new PartnerCompServiceException("Login Error: User or Password Invalid"));
        var jwtToken = jwtService.generateToken(user.getEmailId());
        return generateAuthenticationResponse(user, jwtToken);
    }

    private static AuthenticationResponse generateAuthenticationResponse(User savedUser, String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .employeeId(savedUser.getEmployeeId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .emailId(savedUser.getEmailId())
                .location(savedUser.getLocation())
                .jobTitle(savedUser.getJobTitle())
                .department(savedUser.getDepartment())
                .role(savedUser.getRole())
                .build();
    }
}
