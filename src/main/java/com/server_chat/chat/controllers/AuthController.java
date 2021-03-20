package com.server_chat.chat.controllers;

import com.server_chat.chat.entities.Role;
import com.server_chat.chat.entities.User;
import com.server_chat.chat.services.JwtTokenProvider;
import com.server_chat.chat.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
//@CrossOrigin(origins = "http://37.150.13.55:3000/")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*", exposedHeaders = "If-Match")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        String token = userService.loginUser(username, password);

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", token);
        data.put("tokenType ", "Bearer");
        return ResponseEntity.ok(data);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@RequestParam String username, @RequestParam String password) throws Exception {
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);

        return ResponseEntity.ok(userService.registerUser(u, Role.USER));
    }

    @GetMapping("/current_user/{token}")
    public ResponseEntity<?> getCurrentUser(@PathVariable String token){

        return ResponseEntity.ok(userService.findByUsername(jwtTokenProvider.getClaimsFromJWT(token).getSubject()));
    }
}
