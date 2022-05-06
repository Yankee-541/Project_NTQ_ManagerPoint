package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.ScoreDTO;
import com.example.managerstudentpoint.dto.SubjectDTO;
import com.example.managerstudentpoint.entity.Score;
import com.example.managerstudentpoint.entity.Subject;
import com.example.managerstudentpoint.repository.ScoreRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<Response> getScoresByClassAndCourse(Long key, Integer page, Integer pageSize) {
        Subject subject = new Subject();
        subject.setId(key);
        List<Score> scores = scoreRepository.findAllBySubject(subject, PageRequest.of(page - 1, pageSize)).getContent();
        if (scores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have subject", "")
            );
        } else {
            List<ScoreDTO> scoreDTOS = new ArrayList<>();
            for (Score score : scores) {
                scoreDTOS.add(objectMapper.convertValue(score, ScoreDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("ok", scoreDTOS)
            );
        }
    }
}
