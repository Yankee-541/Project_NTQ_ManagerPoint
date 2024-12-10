package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.SubjectDTO;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.ScoreService;
import com.example.managerstudentpoint.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @Autowired
    ScoreService scoreService;

    @GetMapping("/{nameSubject}/score")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getScoreBySubject(
            @PathVariable String nameSubject,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        return scoreService.getScoresBySubject(nameSubject, page, pageSize);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllSubject(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return subjectService.getSubject(key, page, pageSize);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteSubject(@RequestBody Long[] ids) {
        return subjectService.deleteSubject(ids);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.createSubject(subjectDTO);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateSubject(@RequestBody SubjectDTO subjectDTO) {
        return subjectService.updateSubject(subjectDTO);
    }
}
