package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.RoleDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.RoleService;
import com.example.managerstudentpoint.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/student")
public class MainController {

    @Autowired
    StudentService userService;

    @Autowired
    AuthenService authenService;
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

//    @PostMapping("/create-student")
//    public ResponseEntity<?> createStudent(){
//        ArrayList<User> userDTOS = new ArrayList<>();
//        userDTOS.add(new User("admin","123456","Duy Dang","HE150000","male","hung yen",false,"dang@gmail.com","0988392052"));
//        return authenService.signup(userDTOS);
//    }

//    @PostMapping("/add-role")
//    public ResponseEntity<?> addRole(){
//        ArrayList<RoleDTO> roleDTOS= new ArrayList<>();
//        roleDTOS.add(new RoleDTO("ROLE_ADMIN"));
//        roleDTOS.add(new RoleDTO("ROLE_MODERATOR"));
//        roleDTOS.add(new RoleDTO("ROLE_STUDENT"));
//        for (RoleDTO role: roleDTOS) {
//            RoleDTO roleDTO = new RoleDTO();
//            roleDTO.setName(String.valueOf(role));
//            roleService.addRole(roleDTO);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body("Ok");
//    }

}
