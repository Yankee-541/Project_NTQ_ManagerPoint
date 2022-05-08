package com.example.managerstudentpoint.dto;

import lombok.Data;

@Data
public class CustomAccountDTO {
    private LoginRequestDTO loginRequestDTO;
    private String newPassword;
    private String rePassword;
}
