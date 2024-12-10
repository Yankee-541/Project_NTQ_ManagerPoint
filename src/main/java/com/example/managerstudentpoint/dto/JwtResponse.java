package com.example.managerstudentpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, String username, String email, List<String> roles) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }
}
