package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.JwtUtil.JwtUtils;
import com.example.managerstudentpoint.repository.RoleRepository;
import com.example.managerstudentpoint.repository.StudentRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.Impl.ExportXLSXFileServiceImpl;
import com.example.managerstudentpoint.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/student")
public class StudentController {
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
    ExportXLSXFileServiceImpl exportExcelFileService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//    private final Integer PAGE_SIZE = 5;
    @GetMapping("/list")
    public ResponseEntity<Response> getAllStudents(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return USER_SERVICE.getAllStudents(key, page, pageSize);
    }
}
