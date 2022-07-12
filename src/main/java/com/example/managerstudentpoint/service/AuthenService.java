package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface AuthenService {

    ResponseEntity<?> login(LoginRequestDTO authenRequestDTO);

    ResponseEntity<?> signup(UserDTO studentDTO) throws NoSuchAlgorithmException;

    UserDetails loadUserById(Long userId);

    boolean login_1(UserDTO account);

    ResponseEntity<?> deleteStudent(Long[] id);

    ResponseEntity<Response> updateStudent(UserDTO studentDTO) throws NoSuchAlgorithmException;

    ResponseEntity<Response> changePassword(UserDTO loginRequestDTO, String newPass) throws NoSuchAlgorithmException;

    ResponseEntity<Response> resetPassword(UserDTO userDTO);

    ResponseEntity<?> forgotPasswordSendToMail(UserDTO userDTO) throws MessagingException, IOException;

}
