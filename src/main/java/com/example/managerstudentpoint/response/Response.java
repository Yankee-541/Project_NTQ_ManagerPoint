package com.example.managerstudentpoint.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String message;
    private Object object;

    public Response(String message) {
        this.message = message;
    }
}
