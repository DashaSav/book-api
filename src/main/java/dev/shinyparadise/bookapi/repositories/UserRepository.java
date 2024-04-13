package dev.shinyparadise.bookapi.repositories;

import dev.shinyparadise.bookapi.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
