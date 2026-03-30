package com.example.sitemota.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sitemota.model.User;

public interface UserRepository extends JpaRepository <User,Long> {
 Optional<User> findByEmail(String email);
} 
