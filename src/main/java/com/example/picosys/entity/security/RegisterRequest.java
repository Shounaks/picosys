package com.example.picosys.entity.security;

import com.example.picosys.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String location;
    private String jobTitle;
    private String department;
    private Role role;
}
