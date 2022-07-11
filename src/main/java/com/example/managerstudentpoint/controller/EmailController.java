package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/student")
public class EmailController {

    @Autowired
    AuthenService authenService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordSendToMail(@RequestBody UserDTO userDTO) throws MessagingException, IOException {
        return authenService.forgotPasswordSendToMail(userDTO);
    }
}
