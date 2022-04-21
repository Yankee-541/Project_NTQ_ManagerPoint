package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.JwtUtil.Hashpass;
import com.example.managerstudentpoint.JwtUtil.JwtTokenProvider;
import com.example.managerstudentpoint.dto.AuthenRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.service.AuthenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class AuthenServiceImpl implements AuthenService, UserDetailsService {

    private final UserRepository USER_REPOSITORY;
    private final ObjectMapper OBJECTMAPPER;
    private final JwtTokenProvider JWT_UTIL;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= USER_REPOSITORY.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public String login(AuthenRequestDTO authenRequestDTO) throws NoSuchAlgorithmException {
        User user = OBJECTMAPPER.convertValue(authenRequestDTO, User.class);
        if(USER_REPOSITORY.existsByUsername(authenRequestDTO.getUsername())){
            User user1 = USER_REPOSITORY.findByUsername(user.getUsername());
            String hashpass = Hashpass.hashPassword(authenRequestDTO.getPassword());
            if(hashpass.equals(user1.getPassword())){
                return JWT_UTIL.generateToken(authenRequestDTO.getPassword());
            }else{
                return "login fail. Wrong password !!!!";
            }
        }else{
            return "Username is wrong or invalid";
        }
    }

    @Override
    public UserDetails loadUserById(Long userId) {
        User user = (User) OBJECTMAPPER.convertValue(USER_REPOSITORY.findById(userId).orElse(null),
                UserRepository.class);
        return (UserDetails) user;    }

    @Override
    public String addAccStudent(UserDTO userDTO) throws NoSuchAlgorithmException {
        if (!USER_REPOSITORY.existsByUsername(userDTO.getUsername())
                && !USER_REPOSITORY.existsByPhoneNumber(userDTO.getPhoneNumber())
                && !USER_REPOSITORY.existsByEmail(userDTO.getEmail())){

            userDTO.setPassword(Hashpass.hashPassword(userDTO.getPassword()));
            USER_REPOSITORY.save(OBJECTMAPPER.convertValue(userDTO, User.class));
            return "success";
        }else {
         return "Username is exist";
        }
    }
}
