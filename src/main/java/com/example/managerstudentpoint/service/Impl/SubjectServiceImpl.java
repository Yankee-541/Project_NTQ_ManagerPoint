package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.SubjectDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.Subject;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.SubjectRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<Response> getStudentsBySubject(String key, Integer page, Integer pageSize) {
        List<Subject> subjectList = subjectRepository.getSubjectByNameSubject(true,key,PageRequest.of(page-1, pageSize)).getContent();
        if (subjectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have students","")
            );
        } else {
            List<SubjectDTO> subjectDTOS = new ArrayList<>();
            for (Subject subject : subjectList) {
                subjectDTOS.add(objectMapper.convertValue(subject, SubjectDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("ok",subjectDTOS)
            );
        }
    }


}
