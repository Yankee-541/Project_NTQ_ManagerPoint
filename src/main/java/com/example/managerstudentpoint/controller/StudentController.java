package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.JwtUtil.JwtUtils;
import com.example.managerstudentpoint.dto.CustomAccountDTO;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.ExcelFileService;
import com.example.managerstudentpoint.service.Impl.XLSXFileServiceImpl;
import com.example.managerstudentpoint.service.StudentService;
import com.example.managerstudentpoint.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/student")
public class StudentController {
    @Autowired
    ExcelFileService fileService;

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

    @Autowired
    XLSXFileServiceImpl exportExcelFileService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id) {
        return userService.details(id);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@RequestBody Long[] ids) {
        return authenService.deleteStudent(ids);
    }

    @GetMapping("deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllStudentDeleted(
            @RequestParam(name = "key", defaultValue = "") String key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (page <= 0) {
            page = 1;
        }
        return userService.getAllStudentsDeleted(key, page, pageSize);
    }

    @PutMapping("restore-student/{id}")
    @PreAuthorize("ADMIN")
    public ResponseEntity<?> restoreStudentDeleted(@PathVariable Long id){
        return userService.restoreStudentDeleted(id);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStudent(@Validated @RequestBody UserDTO userDTO)
            throws NoSuchAlgorithmException {
        if (StringUtils.countOccurrencesOf(userDTO.getUsername(), " ") != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Full name is not blank or empty!");
        }
        return authenService.updateStudent(userDTO);
    }

    @GetMapping("/export-score")
    public void exportScoreBySubjectAndClass(
            @Validated
            @RequestParam(name = "rollNumber") String rollnumber) throws IOException {
        List<BaseExportExcelModel> list = new ArrayList<>();
        for (StudentExportExcelDTO user : userService.getScoreByRollnumber(rollnumber)) {
            list.add(user);
        }
        exportExcelFileService.exportScoreByRollnumber(
                "test",
                "repost student",
                list, StudentExportExcelDTO.class);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file){
        return fileService.importStudents(file);
    }

}
