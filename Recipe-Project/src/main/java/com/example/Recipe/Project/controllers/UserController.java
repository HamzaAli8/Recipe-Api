package com.example.Recipe.Project.controllers;

import com.example.Recipe.Project.services.CustomUserDetailService;
import com.example.Recipe.Project.models.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    CustomUserDetailService userDetailService;

    @GetMapping("/user")
    public CustomUserDetails getUser(@CurrentSecurityContext Authentication authentication) {
        return (CustomUserDetails) authentication.getPrincipal();
    }

    @PostMapping("/user")
    public ResponseEntity<?> createNewUser(@RequestBody CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(userDetailService.createNewUser(userDetails));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
