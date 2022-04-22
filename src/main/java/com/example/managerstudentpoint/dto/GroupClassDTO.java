package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupClassDTO {
    private String className;
    private List<User> userList;
}
