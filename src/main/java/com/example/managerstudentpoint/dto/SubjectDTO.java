package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class SubjectDTO {
    private Long id;
    @NotBlank(message = "Course name is not blank!")
    @NotNull(message = "Course name is not null!")
    private String nameSubject;
    private Boolean status;

    private User user;
}
