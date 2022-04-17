package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/search")
    public Page<User> getUser(
            @RequestParam(defaultValue = "") String fullName,
            @RequestParam(defaultValue = "") String rollNumber,
            @RequestParam(defaultValue = "") String gender,
            @RequestParam(defaultValue = "") String address,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String phoneNumber,
            @RequestParam(defaultValue = "3") Integer size,
            @RequestParam(defaultValue = "1") Integer page){
        return userService.getUser(
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


}
