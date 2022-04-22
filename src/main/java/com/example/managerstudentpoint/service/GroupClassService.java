package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

public interface GroupClassService {
    ResponseEntity<Response> classById(Long id);
}
