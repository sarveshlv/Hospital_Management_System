package com.hms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.entities.User;


public interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findByEmail(String email);
}