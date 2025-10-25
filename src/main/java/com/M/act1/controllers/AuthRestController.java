package com.M.act1.controllers;

import com.M.act1.models.User;
import com.M.act1.service.UserService;
import com.M.act1.util.JwtUtil;
import com.M.act1.security.TokenStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenStore tokenStore;

    public AuthRestController(UserService userService, JwtUtil jwtUtil, TokenStore tokenStore) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "username and password required"));
        }

        User user = userService.findByUsername(username);
        if (user == null || !userService.checkPassword(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(username);
        tokenStore.addToken(token); // ✅ store active token

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenStore.removeToken(token); // ✅ invalidate the token
            return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Invalid token"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "invalid user"));
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username already exists"));
        }

        userService.saveUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
