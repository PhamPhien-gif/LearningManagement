package com.example.learning_management.demo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning_management.user.User;

@RestController
@RequestMapping("/test-auth")
public class AuthHelloController {
    
    @GetMapping("/hello")
    public String authHello(@AuthenticationPrincipal User user){
        return "Auth Hello " + user.getName() + " " + user.getEmail() + " " + user.getPassword();
    }
}
