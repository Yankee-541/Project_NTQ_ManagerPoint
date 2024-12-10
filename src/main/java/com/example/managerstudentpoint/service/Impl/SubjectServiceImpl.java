package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.InfoSubjectDTO;
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
        List<Subject> subjectList = subjectRepository.getSubjectByNameSubject(
                false,
                key,
                PageRequest.of(page-1, pageSize)).getContent();
        if (subjectList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        } else {
            List<InfoSubjectDTO> subjectDTOS = new ArrayList<>();
            for (Subject subject : subjectList) {
                subjectDTOS.add(objectMapper.convertValue(subject, InfoSubjectDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(subjectDTOS)
            );
        }
    }

    @Override
    public ResponseEntity<Response> deleteSubject(Long[] ids) {
        for (Long id : ids){
            if(id <= 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));
            }

            Subject subject = subjectRepository.findById(id).orElse(null);
            if (subject != null && !subject.getStatus()){
                subject.setStatus(true);
                subjectRepository.save(subject);
                return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<?> createSubject(SubjectDTO subjectDTO) {
        if(subjectRepository.existsByNameSubject(subjectDTO.getNameSubject())){
            return ResponseEntity
                    .badRequest()
                    .body(new Response(HttpStatus.BAD_REQUEST));
        }
        Subject subject = new Subject(
                subjectDTO.getNameSubject()
        );
        subject.setStatus(false);
        subjectRepository.save(subject);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("OK");
    }

    @Override
    public ResponseEntity<Response> updateSubject(SubjectDTO subjectDTO) {
        Subject subject = subjectRepository.findById(subjectDTO.getId()).orElse(null);
        if(subject == null || subject.getStatus()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        }else {
            subjectDTO.setStatus(subject.getStatus());
            subjectRepository.save(objectMapper.convertValue(subjectDTO,Subject.class));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(subjectDTO)
            );

        }
    }


}
