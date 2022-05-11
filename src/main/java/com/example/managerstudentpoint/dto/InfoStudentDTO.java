package com.example.managerstudentpoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoStudentDTO {
    private String rollNumber;
    private String email;
    private String phoneNumber;
    private String fullName;
    private Set<String> role;
    private String address;
    private Set<String> subject;
}
