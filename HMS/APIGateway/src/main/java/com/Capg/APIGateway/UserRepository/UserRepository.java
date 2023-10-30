package com.Capg.APIGateway.UserRepository;

import com.Capg.APIGateway.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailId(String emailFromToken);
}
