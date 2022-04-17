package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.ReportsDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.ReportRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportRepository reportRepository;

    @Override
    public ResponseEntity<Response> details(Long id) {
        UserDTO userDTO = objectMapper.convertValue(userRepository.findById(id).orElse(null), UserDTO.class);
        ReportsDTO reportsDTO = objectMapper.convertValue(reportRepository.findById(id).orElse(null), ReportsDTO.class);
        if(userDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Not found", "Don't have news with id: " + id, "", "")
            );
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("Found","Found user have id: "+id, userDTO,reportsDTO)
            );
        }
    }

    @Override
    public Page<User> getUser(
            String fullName,
            String rollNumber,
            String gender,
            String address,
            String status,
            String email,
            String phoneNumber,
            Integer size,
            Integer page) {
        return userRepository.getUserByCondition(
                fullName,
                rollNumber,
                gender,
                address,
                status,
                email,
                phoneNumber,
                PageRequest.of(
                        page-1,
                        size,
                        Sort.by("rollNumber").descending())
                );
    }
}
