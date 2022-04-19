package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.GroupClass;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends BaseAbstractDTO<UserDTO> {
    private String username;
    private String password;
    private String fullName;
    private String status;
    private String phoneNumber;
    private String email;
    private String gender;
    private String address;
    private String rollNumber;
//    private GroupClass class_id;
}
