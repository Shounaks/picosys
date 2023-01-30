package com.example.picosys.repository;

import com.example.picosys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.emailId = ?1")
    Optional<User> findByEmailId(String emailId);


}