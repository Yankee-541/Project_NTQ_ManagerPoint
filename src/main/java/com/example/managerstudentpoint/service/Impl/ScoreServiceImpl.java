package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.ScoreDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.dto.SubjectDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.Score;
import com.example.managerstudentpoint.entity.Subject;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.ScoreRepository;
import com.example.managerstudentpoint.repository.SubjectRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

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

    @Override
    public ResponseEntity<Response> addScoreforStudent(ScoreDTO scoreDTO) {
        if (!userRepository.existsByRollNumber(scoreDTO.getUsers().getRollNumber())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Student is not exist!", ""));
        }
        if (!subjectRepository.existsById(scoreDTO.getSubject().getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Subject is not exist!", ""));
        }
        Score score = new Score(
                scoreDTO.getPoint()
        );
        User user = scoreDTO.getUsers();

        Subject subject = scoreDTO.getSubject();

        score.setUsers(user);
        score.setSubject(subject);
        scoreRepository.save(score);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Create sucess", score));
    }

    @Override
    public ResponseEntity<Response> updateScoreforStudent(ScoreDTO scoreDTO) {
        Score score = objectMapper.convertValue(scoreDTO,Score.class);
        scoreRepository.save(score);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Update score sucess", score));
    }

    @Override
    public List<StudentExportExcelDTO> getScoreBySubjectAndClass(Long sub_id, Long c_id) {
        List<StudentExportExcelDTO> studentExportExcelDTO = new ArrayList<>();
        List<Score> scoreList = scoreRepository.getScoresByGroupClassAndSubject(false, sub_id, c_id);
        for (Score score : scoreList) {
            StudentExportExcelDTO studentExportExcelDTO1 = new StudentExportExcelDTO();
            studentExportExcelDTO1.setRollNumber(score.getUsers().getRollNumber());
            studentExportExcelDTO1.setFullName(score.getUsers().getFullName());
            studentExportExcelDTO1.setEmail(score.getUsers().getEmail());
            studentExportExcelDTO1.setGroupClassDTO(score.getUsers().getGroupClass().getClassName());
            studentExportExcelDTO1.setScoreDTO(score.getPoint());
            studentExportExcelDTO1.setSubjectDTO(score.getSubject().getNameSubject());

            studentExportExcelDTO.add(studentExportExcelDTO1);
        }
        return studentExportExcelDTO;
    }

}
