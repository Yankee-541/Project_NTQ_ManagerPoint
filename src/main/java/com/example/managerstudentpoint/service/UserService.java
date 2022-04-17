package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Response> details(Long id);

    Page<User> getUser(
            String fullName,
            String rollNumber,
            String gender,
            String address,
            String status,
            String email,
            String phoneNumber,
            Integer size,
            Integer page);
}
