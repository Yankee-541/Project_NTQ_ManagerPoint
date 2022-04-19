package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.dto.AuthenRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;

public interface AuthenService {
    String login(AuthenRequestDTO authenRequestDTO) throws NoSuchAlgorithmException;

    String addAccStudent(UserDTO userDTO) throws NoSuchAlgorithmException;

}
