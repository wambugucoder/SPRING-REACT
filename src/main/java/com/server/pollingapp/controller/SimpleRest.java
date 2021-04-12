package com.server.pollingapp.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRest {

    @GetMapping(value = "/api/v1/hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok().body("it works");
    }
}
