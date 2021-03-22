package com.server_chat.chat.controllers;

import com.server_chat.chat.entities.InstaUserDetails;
import com.server_chat.chat.entities.Role;
import com.server_chat.chat.entities.User;
import com.server_chat.chat.responces.LoginForm;
import com.server_chat.chat.responces.UserSummary;
import com.server_chat.chat.services.JwtTokenProvider;
import com.server_chat.chat.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginForm) {
        String token = userService.loginUser(loginForm.username, loginForm.password);

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", token);
        data.put("tokenType ", "Bearer");
        return ResponseEntity.ok(data);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@RequestBody LoginForm loginForm) throws Exception {
        User u = new User();
        u.setUsername(loginForm.username);
        u.setPassword(loginForm.password);

        return ResponseEntity.ok(userService.registerUser(u, Role.USER));
    }

    @GetMapping("/current_user/{token}")
    public ResponseEntity<?> getCurrentUser(@PathVariable String token) {

        return ResponseEntity.ok(userService.findByUsername(jwtTokenProvider.getClaimsFromJWT(token).getSubject()));
    }

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) throws Exception {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new Exception(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
                .ok(userService.findAll().stream().map(UserSummary::new).collect(Collectors.toList()));
    }

    @GetMapping(value = "/users/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllUserSummaries(
            @AuthenticationPrincipal InstaUserDetails userDetails) {
        log.info("retrieving all users summaries");

        return ResponseEntity.ok(userService
                .findAll().stream().filter(user -> !user.getUsername().equals(userDetails.getUsername()))
                .map(UserSummary::new)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('USER')")
//    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        User u = userService.findByUsername(userDetails.getUsername()).orElse(new User());
        return ResponseEntity.ok(new UserSummary(u));
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) throws Exception {
        log.info("retrieving user {}", username);

        return userService
                .findByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new Exception(username));
    }

//    private UserSummary convertTo(User user) {
//        return UserSummary
//                .builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .name(user.getUserProfile().getDisplayName())
//                .profilePicture(user.getUserProfile().getProfilePictureUrl())
//                .build();
//    }

}
