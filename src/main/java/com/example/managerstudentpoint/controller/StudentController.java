package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.JwtUtil.JwtUtils;
import com.example.managerstudentpoint.dto.CustomAccountDTO;
import com.example.managerstudentpoint.dto.JwtResponse;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.StudentService;
import com.example.managerstudentpoint.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/student")
public class StudentController {

    @Autowired
    AuthenticationManager authenticationMana;

    @Autowired
    StudentService userService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    JwtUtils jwtToken;

    @Autowired
    AuthenService authenService;

    @GetMapping("/list")
    public ResponseEntity<Response> getAllStudents(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return userService.getAllStudents(key, page, pageSize);
    }

    @PutMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO loginRequest) throws NoSuchAlgorithmException {
//        if (authenService.login_1(loginRequest)) {
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new Response("Login successful!",
//                            new JwtResponse(jwtToken.generateToken(loginRequest.getUsername())))
//            );
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                new Response("Username or password is invalid!")
//        );
        return authenService.login(loginRequest);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteStudent(@RequestBody Long[] ids) {
        return authenService.deleteStudent(ids);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody UserDTO updateStudent) throws NoSuchAlgorithmException {
        return authenService.updateStudent(updateStudent);
    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addStudent(@Validated @RequestBody UserDTO signUpRequest) throws NoSuchAlgorithmException {
        return authenService.signup(signUpRequest);
    }

    @PutMapping("changePassword")
//    @PreAuthorize("hasRole('ADMIN') or #customAccountDTO.userDTO.username == authentication.principal.")
    public ResponseEntity<Response> changePassword(@RequestBody CustomAccountDTO customAccountDTO) throws NoSuchAlgorithmException {
        if (!customAccountDTO.getNewPassword().equals(customAccountDTO.getRePassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response("New password and re-password are not match!")
            );
        }
        return authenService.changePassword(customAccountDTO.getLoginRequestDTO(), customAccountDTO.getNewPassword());
    }

}
