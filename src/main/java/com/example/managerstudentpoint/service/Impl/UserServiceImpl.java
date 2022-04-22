package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.JwtUtil.JwtTokenProvider;
import com.example.managerstudentpoint.dto.ReportsDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.CustomUserDetails;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.ReportRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.UserService;
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
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final ObjectMapper OBJECT_MAPPER;
    private final UserRepository USER_REPOSITORY;
    private final ReportRepository REPORT_REPOSITORY;
    private JwtTokenProvider JWT_UTIL;

    @Override
    public ResponseEntity<Response> details(Long id) {
        UserDTO userDTO = OBJECT_MAPPER.convertValue(USER_REPOSITORY.findById(id).orElse(null), UserDTO.class);
        ReportsDTO reportsDTO = OBJECT_MAPPER.convertValue(REPORT_REPOSITORY.findById(id).orElse(null), ReportsDTO.class);
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
        return USER_REPOSITORY.getUserByCondition(
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

//    @Override
//    public UserDTO addAccStudent(UserDTO userDTO) {
//        USER_REPOSITORY.save(OBJECT_MAPPER.convertValue(userDTO,User.class));
//        return userDTO;
//    }

    @Override
    public List<User> listAll() {
        return USER_REPOSITORY.findAll(Sort.by("email").ascending());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = USER_REPOSITORY.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);

    }
    @Override
    public UserDetails loadUserById(Long userId) {
        User user =USER_REPOSITORY.findById(userId).orElse(null);
        return new CustomUserDetails(user);
    }
}
