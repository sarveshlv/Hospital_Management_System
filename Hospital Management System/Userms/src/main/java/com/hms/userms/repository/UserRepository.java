package com.hms.userms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.userms.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
