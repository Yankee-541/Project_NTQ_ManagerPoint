package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.ScoreDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScoreService {

    ResponseEntity<Response> getScoresBySubject(String key, Integer page, Integer pageSize);

    ResponseEntity<?> addScoreforStudent(ScoreDTO scoreDTO);

    ResponseEntity<Response> updateScoreforStudent(ScoreDTO scoreDTO);

    List<StudentExportExcelDTO> getScoreBySubjectAndClass(Long sub_id, Long c_id);



}
