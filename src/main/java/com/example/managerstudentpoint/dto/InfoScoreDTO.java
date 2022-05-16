package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.Subject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoScoreDTO {    private Long id;

    private Double point;

    private Subject subject;

    private InfoStudentDTO users;

}
