package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.GroupClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/class")
@AllArgsConstructor
public class GroupClassController {
    private final GroupClassService groupClassService;
    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id) {
        return groupClassService.classById(id);
    }

}
