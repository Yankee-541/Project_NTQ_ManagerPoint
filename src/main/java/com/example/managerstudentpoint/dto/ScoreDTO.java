package com.example.managerstudentpoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO {
    private Long id;
    private Double point;
    private UserDTO users;
    private SubjectDTO subject;

}
