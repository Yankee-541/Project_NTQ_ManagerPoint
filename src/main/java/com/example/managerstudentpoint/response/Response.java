package com.example.managerstudentpoint.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private String status;
    private String message;
    private Object object;
    private Object ob2;
}
