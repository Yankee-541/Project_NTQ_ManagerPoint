package com.example.managerstudentpoint.dto;

import lombok.Data;
@Data
public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(){
        super();
    }

    public LoginRequestDTO(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
}
