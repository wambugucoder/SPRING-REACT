package com.server.pollingapp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UnauthorizedController {

    /**
     * Instead of user being redirected back to oauth login form after being unauthenicated
     * This Api will prevent that and send a 401 unauthorzed.
     * @return ResponseEntity-> unauthorized
     */

    @GetMapping("api/v1/unauthorized/auth")
    public ResponseEntity<?> noAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("message", "unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
