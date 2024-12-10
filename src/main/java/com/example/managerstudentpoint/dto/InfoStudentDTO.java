package com.example.managerstudentpoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoStudentDTO {

    private Long id;
    private String rollNumber;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String address;

}
