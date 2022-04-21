package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.AuthenRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.exportExcel.StudentExportExcel;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/student")
@AllArgsConstructor
public class MainController {

    private final UserService USER_SERVICE;
    private final AuthenService AUTHEN_SERVICE;

    @GetMapping("")
    public Page<User> getUser(
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "") String rollNumber,
            @RequestParam(defaultValue = "") String gender,
            @RequestParam(defaultValue = "") String address,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String phoneNumber,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "page") Integer page) {
        return USER_SERVICE.getUser(
                fullName,
                rollNumber,
                gender,
                address,
                status,
                email,
                phoneNumber,
                size,
                page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id) {
        return USER_SERVICE.details(id);
    }

    @PutMapping("/register")
//    String addAccStudent(@Valid @RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
//        return AUTHEN_SERVICE.addAccStudent(userDTO);
//    }
    public ResponseEntity<Response> addAccStudent(@Valid @RequestBody UserDTO userDTO) throws NoSuchAlgorithmException {
//        AUTHEN_SERVICE.addAccStudent(userDTO);
        return AUTHEN_SERVICE.addAccStudent(userDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenRequestDTO authenRequest) throws NoSuchAlgorithmException {
        return AUTHEN_SERVICE.login(authenRequest);
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<User> userList = USER_SERVICE.listAll();
        StudentExportExcel studentExportExcel = new StudentExportExcel(userList);
        studentExportExcel.export(response);
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
