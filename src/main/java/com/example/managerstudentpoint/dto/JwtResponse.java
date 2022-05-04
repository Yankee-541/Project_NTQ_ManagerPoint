package com.example.managerstudentpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
//    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String message;

    public JwtResponse(String token, Long id, String username, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String message) {
        this.message = message;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }
}
