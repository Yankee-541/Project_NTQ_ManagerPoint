package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.CustomAccountDTO;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/auth")
public class AuthorController {

    @Autowired
    AuthenService authenService;

    @PutMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO loginRequest) {
        return authenService.login(loginRequest);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addStudent(@Validated @RequestBody UserDTO userDTO)
            throws NoSuchAlgorithmException {
        if (StringUtils.countOccurrencesOf(userDTO.getUsername(), " ") != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Full name is not blank or empty!");
        }
        return authenService.signup(userDTO);
    }

    @PutMapping("change-password")
    public ResponseEntity<Response> changePassword(@RequestBody CustomAccountDTO customAccountDTO)
            throws NoSuchAlgorithmException {
        if (!customAccountDTO.getNewPassword().equals(customAccountDTO.getRePassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("New password and re-password are not match!")
            );
        }
        return authenService.changePassword(customAccountDTO.getLoginRequestDTO(), customAccountDTO.getNewPassword());
    }

    @PutMapping("reset-password")
    public ResponseEntity<Response> resetPassword(@RequestBody UserDTO userDTO){
        return authenService.resetPassword(userDTO);
    }

}
