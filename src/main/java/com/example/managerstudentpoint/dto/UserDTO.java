package com.example.managerstudentpoint.dto;

import javax.validation.constraints.NotBlank;

import com.example.managerstudentpoint.entity.GroupClass;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseAbstractDTO<UserDTO> {
    private Long id;
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Fullname is mandatory")
    private String fullName;
    private Boolean isDelete;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String gender;
    private String address;
    private String rollNumber;
    private GroupClass groupClass;
    private Set<String> role;
    private Set<String> subject;
}
