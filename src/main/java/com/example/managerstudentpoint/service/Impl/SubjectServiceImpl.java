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
    public ResponseEntity<Response> getSubject(String key, Integer page, Integer pageSize) {
        List<Subject> subjectList = subjectRepository.getSubjectByNameSubject(false,key,PageRequest.of(page-1, pageSize)).getContent();
        if (subjectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have subject","")
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

    @Override
    public ResponseEntity<String> deleteSubject(Long[] ids) {
        for (Long id : ids){
            Subject subject = subjectRepository.findById(id).orElse(null);
            if (subject == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found with id");
            }
            subject.setStatus(true);
            subjectRepository.save(subject);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete success");


    }


}
