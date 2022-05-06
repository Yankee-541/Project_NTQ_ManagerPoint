package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.JwtUtil.JwtUtils;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.repository.RoleRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.ScoreService;
import com.example.managerstudentpoint.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/student")
public class MainController {
    @Autowired
    AuthenticationManager authenticationMana;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    StudentService USER_SERVICE;

    @Autowired
    AuthenService AUTHEN_SERVICE;

    @Autowired
    UserRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ScoreService scoreService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id) {
        return USER_SERVICE.details(id);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStudent(@RequestBody Long[] ids) {
        return AUTHEN_SERVICE.deleteStudent(ids);
    }

    @PutMapping("/signin")
    public ResponseEntity<?> signin(@Validated @RequestBody LoginRequestDTO loginRequest) throws NoSuchAlgorithmException {
        return AUTHEN_SERVICE.login(loginRequest);

    }

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserDTO signUpRequest) throws NoSuchAlgorithmException {
        return AUTHEN_SERVICE.signup(signUpRequest);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody UserDTO updateStudent) throws NoSuchAlgorithmException {
        return AUTHEN_SERVICE.updateStudent(updateStudent);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
