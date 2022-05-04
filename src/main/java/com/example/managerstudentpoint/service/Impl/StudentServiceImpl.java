package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.CustomUserDetails;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.entity.UserDetailsImpl;
import com.example.managerstudentpoint.repository.StudentRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final ObjectMapper OBJECT_MAPPER;
    private final StudentRepository USER_REPOSITORY;

    @Override
    public ResponseEntity<Response> details(Long id) {
        UserDTO userDTO = OBJECT_MAPPER.convertValue(USER_REPOSITORY.findById(id).orElse(null), UserDTO.class);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have news with id: " + id, "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("Found user have id: " + id, userDTO)
            );
        }
    }

    @Override
    public ResponseEntity<Response> getAllStudents(String key, Integer page, Integer pageSize) {
        List<User> studentList = USER_REPOSITORY.getUsersAllByFullNameAndRollNumberAndUsername(
                "deactive",
                key,
                PageRequest.of(page - 1, pageSize)).getContent();
        if (studentList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have students","")
            );
        } else {
            List<UserDTO> userDTOList = new ArrayList<>();
            for (User student : studentList) {
                userDTOList.add(OBJECT_MAPPER.convertValue(student, UserDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("",userDTOList)
            );
        }
    }

    @Override
    public List<StudentExportExcelDTO> listAll() {
        List<StudentExportExcelDTO> studentExportExcelDTO = new ArrayList<>();
        List<User> userList = USER_REPOSITORY.findAll();
        for (User user : userList) {
            StudentExportExcelDTO studentExportExcelDTO1 = OBJECT_MAPPER.convertValue(
                    user,
                    StudentExportExcelDTO.class);
            studentExportExcelDTO.add(studentExportExcelDTO1);
        }
        return studentExportExcelDTO;
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
        return USER_REPOSITORY.getUserByCondition(
                fullName,
                rollNumber,
                gender,
                address,
                status,
                email,
                phoneNumber,
                PageRequest.of(
                        page - 1,
                        size,
                        Sort.by("rollNumber").descending())
        );
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User student = USER_REPOSITORY.findByUsername(username);
        if (student == null) {
            throw new UsernameNotFoundException(username);
        }
        return UserDetailsImpl.built(student);
    }


    @Override
    public UserDetails loadUserById(Long userId) {
        User user = USER_REPOSITORY.findById(userId).orElse(null);
        return new CustomUserDetails(user);
    }
}
