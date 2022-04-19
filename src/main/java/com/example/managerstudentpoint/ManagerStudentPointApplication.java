package com.example.managerstudentpoint;

import com.example.managerstudentpoint.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication(exclude ={SecurityAutoConfiguration.class})
//@SpringBootApplication
public class ManagerStudentPointApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerStudentPointApplication.class, args);
    }
//    aasdf
}
