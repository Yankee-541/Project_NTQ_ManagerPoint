package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

public interface ScoreService {
    ResponseEntity<Response> getScoresByClassAndCourse(Long key, Integer page, Integer pageSize);
}
