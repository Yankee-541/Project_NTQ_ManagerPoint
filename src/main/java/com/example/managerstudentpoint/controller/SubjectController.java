package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    @GetMapping("/listStudentBySubject")
    public ResponseEntity<Response> getAllSubject(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return subjectService.getStudentsBySubject(key, page, pageSize);
    }



}
