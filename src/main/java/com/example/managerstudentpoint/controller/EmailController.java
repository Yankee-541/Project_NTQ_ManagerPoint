package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@RestController
@RequestMapping(value = "api/student")
public class EmailController {

    @Autowired
    AuthenService authenService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordSendToMail(@RequestBody UserDTO userDTO) throws MessagingException, IOException {
        System.out.println();
        return authenService.forgotPasswordSendToMail(userDTO);
    }
}
