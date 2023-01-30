package com.example.picosys.controller;

import com.example.picosys.entity.security.AuthenticationResponse;
import com.example.picosys.entity.security.LoginRequest;
import com.example.picosys.entity.security.RegisterRequest;
import com.example.picosys.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/digitalbooks/authentication/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("sign-in")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("sign-up")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }
}
