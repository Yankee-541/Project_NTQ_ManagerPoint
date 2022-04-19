package com.example.managerstudentpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenRequestDTO {
    private String username;
    private String password;
}
