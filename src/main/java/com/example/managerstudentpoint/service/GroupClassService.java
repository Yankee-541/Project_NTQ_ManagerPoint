package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface GroupClassService {
    ResponseEntity<Response> classById(Long id);
    ResponseEntity<Response> addClass(GroupClassDTO groupClassDTO);
    ResponseEntity<String> deleteClass(Long[] id);


}
