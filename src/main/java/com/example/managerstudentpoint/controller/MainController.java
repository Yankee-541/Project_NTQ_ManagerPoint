package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
@AllArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id){
        return userService.details(id);
    }

}
