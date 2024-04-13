package dev.shinyparadise.bookapi.auth;

import dev.shinyparadise.bookapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private User user;
    private String token;
}
