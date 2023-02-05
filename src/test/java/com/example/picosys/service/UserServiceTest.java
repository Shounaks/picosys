package com.example.picosys.service;

import com.example.picosys.entity.User;
import com.example.picosys.entity.security.AuthenticationResponse;
import com.example.picosys.entity.security.LoginRequest;
import com.example.picosys.entity.security.RegisterRequest;
import com.example.picosys.exceptions.PartnerCompServiceException;
import com.example.picosys.repository.UserRepository;
import com.example.picosys.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    UserService userService;

    @BeforeEach
    public final void setup() {
        userService = new UserService(userRepository, new BCryptPasswordEncoder(), new JwtService(), authenticationManager);
    }

    @Test
    void addUser_ValidTest() {
        JwtService jwtService = new JwtService();
        //Expected
        User expectedUser = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .emailId("test@gmail.com")
                .password("qwerty")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        //Mocks
        Mockito.when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(expectedUser);

        //When
        AuthenticationResponse response = userService.registerUser(registerRequest);

        //Then
        assertThat(response.getEmployeeId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo(registerRequest.getFirstName());
        assertThat(response.getEmailId()).isEqualTo(registerRequest.getEmailId());
        assertThat(response.getToken()).isEqualTo(jwtService.generateToken(registerRequest.getEmailId()));

    }

    @Test
    void addUser_InValidTest() {
        //Expected
        String errorMsg = "User Registration Error: Email Already Registered";
        User expectedUser = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .emailId("test@gmail.com")
                .password("qwerty")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        //Mocks
        Mockito.when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(expectedUser));

        //Then
        assertThatThrownBy(() -> userService.registerUser(registerRequest), errorMsg)
                .isInstanceOf(PartnerCompServiceException.class);
    }

    @Test
    void loginUser_ValidTest() {
        //Expected
        User expectedUser = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        LoginRequest registerRequest = LoginRequest.builder()
                .emailId("test@gmail.com")
                .password("qwerty")
                .build();

        //Mocks
        Mockito.when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(expectedUser));
        Mockito.when(userRepository.save(any())).thenReturn(expectedUser);

        //When
        AuthenticationResponse response = userService.loginUser(registerRequest);

        //Then
        assertThat(response.getEmployeeId()).isEqualTo(1L);
        assertThat(response.getEmailId()).isEqualTo(registerRequest.getEmailId());
    }

    @Test
    void loginUser_InValidTest() {
        String errorMsg = "Login Error: User or Password Invalid";
        //Expected
        User expectedUser = User.builder()
                .employeeId(1L)
                .password("qwerty")
                .emailId("test@gmail.com")
                .firstName("Tester One")
                .lastName("Tester Two")
                .build();

        LoginRequest registerRequest = LoginRequest.builder()
                .emailId("test@gmail.com")
                .password("qwerty")
                .build();

        //Mocks
        Mockito.when(userRepository.findByEmailId(anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(expectedUser);

        //Then
        assertThatThrownBy(() -> userService.loginUser(registerRequest), errorMsg).isInstanceOf(PartnerCompServiceException.class);
    }

    @Test
    void getAllCompensationPlans_validTest(){
        Mockito.when(userRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(User.builder().build(),User.builder().build())));
        Page<User> allUsers = userService.retrieveAllUsers(0);
        assertThat(allUsers).isNotNull();
        assertThat(allUsers.getContent()).isNotNull();
    }

    @Test
    void retrieveUserById_validTest(){
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(User.builder().build()));
        Optional<User> user = userService.retrieveUserById(1L);
        assertThat(user).isPresent();
    }
    @Test
    void retrieveUserByEmail_validTest(){
        Mockito.when(userRepository.findByEmailId(anyString())).thenReturn(Optional.ofNullable(User.builder().build()));
        Optional<User> user = userService.retrieveUserByEmail("DUMMY@EMAIL.com");
        assertThat(user).isPresent();
    }
}