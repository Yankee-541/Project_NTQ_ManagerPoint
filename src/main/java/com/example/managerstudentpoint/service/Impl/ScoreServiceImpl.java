package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.InfoScoreDTO;
import com.example.managerstudentpoint.dto.InfoStudentDTO;
import com.example.managerstudentpoint.dto.ScoreDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
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
    public ResponseEntity<Response> getScoresBySubject(String key, Integer page, Integer pageSize) {
        List<Score> scores = scoreRepository.findAllScoreBySubject(
                false,
                key,
                PageRequest.of(page - 1, pageSize)).getContent();
        if (scores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
        } else {
            List<InfoScoreDTO> scoreDTOS = new ArrayList<>();
            for (Score score : scores){
                InfoScoreDTO scoreDTO = new InfoScoreDTO();
                InfoStudentDTO infoStudentDTO = objectMapper.convertValue(scoreDTO.getUsers(), InfoStudentDTO.class);
                scoreDTO.setUsers(infoStudentDTO);
                scoreDTOS.add(objectMapper.convertValue(score, InfoScoreDTO.class));

            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(scoreDTOS)
            );
        }
    }

    @Override
    public ResponseEntity<?> addScoreforStudent(ScoreDTO scoreDTO) {
        if (!userRepository.existsByIdAndIsDelete(scoreDTO.getUsers().getId(), false)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found student");
        }
        if (!subjectRepository.existsByIdAndStatus(scoreDTO.getSubject().getId(), false)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found subject");
        }
        if (scoreDTO.getPoint() < 0 || scoreDTO.getPoint() > 10) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response(HttpStatus.BAD_REQUEST));
        }
        if (scoreRepository.findAllByUsers_IdAndSubject_Id(scoreDTO.getUsers().getId(), scoreDTO.getSubject().getId()) == null) {
            Score score = new Score(
                    scoreDTO.getPoint()
            );
            User user = scoreDTO.getUsers();
            score.setUsers(user);
            Subject subject = scoreDTO.getSubject();
            score.setSubject(subject);
            scoreRepository.save(score);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
        } else {
            return ResponseEntity.badRequest().body("Already exist");
        }
    }

    @Override
    public ResponseEntity<Response> updateScoreforStudent(ScoreDTO scoreDTO) {
        if (!userRepository.existsByIdAndIsDelete(scoreDTO.getUsers().getId(), false)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Not found student"));
        }
        if (!subjectRepository.existsByIdAndStatus(scoreDTO.getSubject().getId(), false)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Not found subject"));
        }
        if (scoreDTO.getPoint() < 0 || scoreDTO.getPoint() > 10) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Score student must be >0 and <= 10 !!!"));
        }
        Score score = objectMapper.convertValue(scoreDTO, Score.class);
        scoreRepository.save(score);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
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
