package dev.shinyparadise.bookapi.auth;

import dev.shinyparadise.bookapi.jwt.JwtService;
import dev.shinyparadise.bookapi.models.User;
import dev.shinyparadise.bookapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public Optional<AuthResponse> register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        var existingUser = repository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
           return Optional.empty();
        }

        var dbUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return Optional.of(new AuthResponse(dbUser, jwtToken));
    }

    public Optional<AuthResponse> login(AuthRequest request) {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getEmail(),
                           request.getPassword()
                   )
           );
           var dbUser = repository.findByEmail(request.getEmail()).orElseThrow();

           var jwtToken = jwtService.generateToken(dbUser);

           return Optional.of(new AuthResponse(dbUser, jwtToken));
       } catch (Exception e) {
           return Optional.empty();
       }
    }
}
