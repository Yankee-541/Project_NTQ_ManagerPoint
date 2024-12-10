package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.InfoStudentDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.CustomUserDetails;
import com.example.managerstudentpoint.entity.Score;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.entity.UserDetailsImpl;
import com.example.managerstudentpoint.repository.ScoreRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService, UserDetailsService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;


    @Override
    public ResponseEntity<Response> details(Long id) {
        InfoStudentDTO userDTO = objectMapper.convertValue(userRepository.findByIdAndIsDelete(id,false), InfoStudentDTO.class);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have news with id: " + id)
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(userDTO)
            );
        }
    }

    @Override
    public ResponseEntity<Response> getAllStudents(String key, Integer page, Integer pageSize) {
        List<User> studentList = userRepository.getUsersAllByFullNameAndRollNumberAndUsername(
                false,
                key,
                PageRequest.of(page - 1, pageSize)).getContent();
        if (studentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.BAD_REQUEST)
            );
        } else {
            List<InfoStudentDTO> userDTOList = new ArrayList<>();
            for (User user : studentList) {
                userDTOList.add(objectMapper.convertValue(user, InfoStudentDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(userDTOList)
            );
        }
    }

    @Override
    public ResponseEntity<Response> getAllStudentsDeleted(String key, Integer page, Integer pageSize) {
        List<User> studentList = userRepository.getUsersAllByFullNameAndRollNumberAndUsername(
                true,
                key,
                PageRequest.of(page - 1, pageSize)).getContent();
        if (studentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        } else {
            List<InfoStudentDTO> userDTOList = new ArrayList<>();
            for (User user : studentList) {
                userDTOList.add(objectMapper.convertValue(user, InfoStudentDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(userDTOList)
            );
        }
    }

    @Override
    public ResponseEntity<?> restoreStudentDeleted(Long id) {
        if (userRepository.existsById(id)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        }


        return null;
    }

    @Override
    public List<StudentExportExcelDTO> listAll() {
        List<StudentExportExcelDTO> studentExportExcelDTO = new ArrayList<>();
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            StudentExportExcelDTO studentExportExcelDTO1 = objectMapper.convertValue(
                    user,
                    StudentExportExcelDTO.class);
            studentExportExcelDTO.add(studentExportExcelDTO1);
        }
        return studentExportExcelDTO;
    }

    @Override
    public List<StudentExportExcelDTO> getScoreByRollnumber(String rollnumber) {
        List<StudentExportExcelDTO> studentExportExcelDTO = new ArrayList<>();
        List<Score> scoreList = scoreRepository.getScoresByUsers(false, rollnumber);
        for (Score score : scoreList) {
            StudentExportExcelDTO studentExportExcelDTO1 = new StudentExportExcelDTO();
            studentExportExcelDTO1.setRollNumber(score.getUsers().getRollNumber());
            studentExportExcelDTO1.setFullName(score.getUsers().getFullName());
            studentExportExcelDTO1.setEmail(score.getUsers().getEmail());
            studentExportExcelDTO1.setScoreDTO(score.getPoint());
            studentExportExcelDTO1.setSubjectDTO(score.getSubject().getNameSubject());

            studentExportExcelDTO.add(studentExportExcelDTO1);
        }
        return studentExportExcelDTO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User student = userRepository.findByUsername(username);
        if (student == null) {
            throw new UsernameNotFoundException(username);
        }
        return UserDetailsImpl.built(student);
    }


    @Override
    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return new CustomUserDetails(user);
    }
}
