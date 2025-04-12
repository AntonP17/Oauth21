package com.example.oauth25.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "hello, Home!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "hello, Secured!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "hello, Admin!";
    }

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal OAuth2User user) {
        Optional<Map<String, Object>> attributes = Optional.ofNullable(user.getAttributes());
        String name = attributes
                .flatMap(attrs -> Optional.ofNullable(attrs.get("name")))
                .map(Object::toString)
                .orElse("User");

        return "hello, " + name;
    }
}
