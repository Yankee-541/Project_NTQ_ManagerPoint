package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.Excel.ExcelHelper;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/student")
public class AuthorController {
    @Autowired
    ExcelFileService fileService;

//    @PostMapping("/import")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Response> importFile(@RequestParam("file") MultipartFile file) {
//        String message = "";
//        if (ExcelHelper.hasExcelFormat(file)) {
//            try {
//                fileService.importStudents(file);
//                message = "Uploaded the file successfully: " + file.getOriginalFilename();
//                return ResponseEntity.status(HttpStatus.OK).body(new Response(message));
//            } catch (Exception e) {
//                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(message));
//            }
//        }
//        message = "Please upload an excel file!";
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(message));
//    }
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/import")
    public ResponseEntity<String> importStudents(@RequestParam("file") MultipartFile file){
        return fileService.importStudents(file);
    }

}
