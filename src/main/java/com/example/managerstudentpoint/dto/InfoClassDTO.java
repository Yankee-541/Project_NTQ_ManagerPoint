package com.example.managerstudentpoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoClassDTO {
    private Long id;
    private String className;
    private InfoStudentDTO users;

}
