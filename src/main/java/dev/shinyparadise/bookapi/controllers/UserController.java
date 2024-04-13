package dev.shinyparadise.bookapi.controllers;

import dev.shinyparadise.bookapi.models.User;
import dev.shinyparadise.bookapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public final List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public final ResponseEntity<?> getById(@PathVariable String id) {
        try {
            var user = userRepository.findById(id).orElseThrow();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public final ResponseEntity<?> update(@RequestBody User user) {
        try {
            var userFromDb = userRepository.findById(user.getId()).orElseThrow();

            userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
            userFromDb.setEmail(user.getEmail());
            userFromDb.setName(user.getName());

            var updatedUser = userRepository.save(userFromDb);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            var errorResponse = new ErrorResponse(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}

