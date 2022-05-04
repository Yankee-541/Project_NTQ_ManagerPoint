package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.JwtResponse;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;

public interface AuthenService {
    ResponseEntity<JwtResponse> login(LoginRequestDTO authenRequestDTO) throws NoSuchAlgorithmException;

    ResponseEntity<Response> signup(UserDTO studentDTO) throws NoSuchAlgorithmException;

    UserDetails loadUserById(Long userId);

    ResponseEntity<Response> addAccStudent(UserDTO userDTO) throws NoSuchAlgorithmException;

    HttpStatus deleteStudent(Long[] id);

    ResponseEntity<Response> updateStudent(UserDTO studentDTO) throws NoSuchAlgorithmException;

}
