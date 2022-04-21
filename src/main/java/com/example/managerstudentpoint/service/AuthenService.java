package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.AuthenRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;

public interface AuthenService {
    String login(AuthenRequestDTO authenRequestDTO) throws NoSuchAlgorithmException;
    UserDetails loadUserById(Long userId);
    ResponseEntity<Response> addAccStudent(UserDTO userDTO) throws NoSuchAlgorithmException;
//    String addAccStudent(UserDTO userDTO) throws NoSuchAlgorithmException;

}
