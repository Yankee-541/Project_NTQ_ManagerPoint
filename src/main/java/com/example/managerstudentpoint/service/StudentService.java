package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface StudentService {

    ResponseEntity<Response> details(Long id);

    ResponseEntity<Response> getAllStudents(String key, Integer page, Integer pageSize);

    ResponseEntity<Response> getAllStudentsDeleted(String key, Integer page, Integer pageSize);

    ResponseEntity<?> restoreStudentDeleted(Long id);

    UserDetails loadUserById(Long userId);

    List<StudentExportExcelDTO> listAll();

    List<StudentExportExcelDTO> getScoreByRollnumber(String rollnumber);

}
