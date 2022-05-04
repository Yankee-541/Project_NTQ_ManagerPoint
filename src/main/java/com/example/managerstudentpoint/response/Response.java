package com.example.managerstudentpoint.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String status;
    private String message;
    private Object object;

    public Response(String message) {
        this.message = message;
    }

    public Response(Object object) {
        this.object = object;
    }
}
