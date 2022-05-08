package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

public interface SubjectService {
    ResponseEntity<Response> getSubject(String key, Integer page, Integer pageSize);
    ResponseEntity<String> deleteSubject(Long[] ids);
}
