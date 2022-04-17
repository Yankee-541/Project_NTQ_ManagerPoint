package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.Subject;
import com.example.managerstudentpoint.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportsDTO extends BaseAbstractDTO<ReportsDTO> {
    private Double point;
    private User users;
    private Subject subject;
}
