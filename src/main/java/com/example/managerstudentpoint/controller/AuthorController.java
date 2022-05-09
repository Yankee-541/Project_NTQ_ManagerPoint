package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/student")
public class AuthorController {
    @Autowired
    ExcelFileService fileService;

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file){
        return fileService.importStudents(file);
    }

}
