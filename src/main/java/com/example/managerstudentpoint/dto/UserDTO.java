package com.example.managerstudentpoint.dto;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseAbstractDTO<UserDTO> {
    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Fullname is mandatory")
    private String fullName;
    private String status;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String gender;
    private String address;
    private String rollNumber;
//    private GroupClass class_id;
}
