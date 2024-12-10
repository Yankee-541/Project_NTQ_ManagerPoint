package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.SubjectDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

public interface SubjectService {
    ResponseEntity<Response> getSubject(String key, Integer page, Integer pageSize);

    ResponseEntity<Response> deleteSubject(Long[] ids);

    ResponseEntity<?> createSubject(SubjectDTO subjectDTO);

    ResponseEntity<Response> updateSubject(SubjectDTO subjectDTO);


}
