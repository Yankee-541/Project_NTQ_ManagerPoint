package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @GetMapping("/ScoreBySubject")
    public ResponseEntity<Response> getScoreSubject(
            @RequestParam(name = "key", defaultValue = "") Long key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return scoreService.getScoresByClassAndCourse(key, page, pageSize);
    }
}
