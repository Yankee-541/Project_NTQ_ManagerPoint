package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface GroupClassService {
    ResponseEntity<Response> getStudentByClassId(String groupClass);

    ResponseEntity<Response> addClass(GroupClassDTO groupClassDTO);

    ResponseEntity<Response> deleteClass(Long[] id);

    ResponseEntity<Response> updateClass(GroupClassDTO groupClassDTO);

    ResponseEntity<Response> listClass(String key);

}
