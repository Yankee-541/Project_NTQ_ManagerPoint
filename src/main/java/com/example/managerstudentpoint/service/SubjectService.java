package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

public interface SubjectService {
    ResponseEntity<Response> getStudentsBySubject(String key, Integer page, Integer pageSize);
}
