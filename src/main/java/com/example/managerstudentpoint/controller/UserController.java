package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.JwtUtil.JwtUtil;
import com.example.managerstudentpoint.dto.AuthenRequestDTO;
import com.example.managerstudentpoint.service.Impl.AuthenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RequestMapping("api/")
@RestController
@AllArgsConstructor
public class UserController {
//    @Autowired
//    UserService userService;
//
//    @GetMapping("/search")
//    public Page<User> getUser(
//            @RequestParam(defaultValue = "") String fullName,
//            @RequestParam(defaultValue = "") String rollNumber,
//            @RequestParam(defaultValue = "") String gender,
//            @RequestParam(defaultValue = "") String address,
//            @RequestParam(defaultValue = "") String status,
//            @RequestParam(defaultValue = "") String email,
//            @RequestParam(defaultValue = "") String phoneNumber,
//            @RequestParam(defaultValue = "5") Integer size,
//            @RequestParam(defaultValue = "1") Integer page){
//        return userService.getUser(
//                fullName,
//                rollNumber,
//                gender,
//                address,
//                status,
//                email,
//                phoneNumber,
//                size,
//                page);
//    }

//    private JwtUtil jwtUtil;
//    private AuthenticationManager authenticationManager;
    private final AuthenServiceImpl AUTHEN_SERVICE;

    @PostMapping("/authen")
    public String generateToken(@RequestBody AuthenRequestDTO authenRequest) throws NoSuchAlgorithmException {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authenRequest.getUsername(),authenRequest.getPassword())
//            );
//        }catch (Exception e){
//            throw new Exception("invalid");
//        }
//        return jwtUtil.generateToken(authenRequest.getUsername());
        return AUTHEN_SERVICE.login(authenRequest);

    }
}
