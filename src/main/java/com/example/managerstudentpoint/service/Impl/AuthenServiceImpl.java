package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.JwtUtil.JwtUtils;
import com.example.managerstudentpoint.common.ERole;
import com.example.managerstudentpoint.dto.JwtResponse;
import com.example.managerstudentpoint.dto.LoginRequestDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.Role;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.entity.UserDetailsImpl;
import com.example.managerstudentpoint.repository.GroupClassRepository;
import com.example.managerstudentpoint.repository.RoleRepository;
import com.example.managerstudentpoint.repository.StudentRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
import com.example.managerstudentpoint.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenServiceImpl implements AuthenService, UserDetailsService {

    StudentRepository studentRepository;
    ObjectMapper objectmapper;
    AuthenticationManager authenticationMana;
    JwtUtils jwtUtils;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    StudentService studentService;
    GroupClassRepository groupClassRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = studentRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public ResponseEntity<Response> signup(UserDTO signUpRequest){
        if (studentRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Username is already taken!",""));
        }
        if (studentRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Email is already in use!",""));
        }
        if (!groupClassRepository.existsById(signUpRequest.getGroupClass().getId())){
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Group class isn't exist!",""));
        }
        User user = new User(
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                generateRollNumber(),
                signUpRequest.getGender(),
                signUpRequest.getAddress(),
                signUpRequest.getStatus(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber()
        );

        GroupClass groupclass = signUpRequest.getGroupClass();
        if (groupClassRepository.existsById(signUpRequest.getGroupClass().getId())){
            user.setGroupClass(groupclass);
        }
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoleList(roles);
        studentRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Create sucess",user));
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequestDTO loginRequest) throws NoSuchAlgorithmException {
        if (studentRepository.existsByUsername(loginRequest.getUsername())) {
            Authentication authentication = authenticationMana.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles
            ));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Username is wrong or invalid"));
        }
    }


    @Override
    public UserDetails loadUserById(Long userId) {
        User user = (User) objectmapper.convertValue(studentRepository.findById(userId).orElse(null),
                StudentRepository.class);
        return (UserDetails) user;
    }

    @Override
    public ResponseEntity<Response> addAccStudent(UserDTO userDTO) {
        if (!studentRepository.existsByUsername(userDTO.getUsername())) {
            if (!studentRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
                if (!studentRepository.existsByEmail(userDTO.getEmail())) {
                    userDTO.setRollNumber(generateRollNumber());
                    studentRepository.save(objectmapper.convertValue(userDTO, User.class));
                    return ResponseEntity.status(HttpStatus.OK).body(new Response("Create sucess", userDTO));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new Response("Email is exist", ""));
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Phone number is exist",""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Username is exist", "")
            );
        }
    }

    @Override
    public HttpStatus deleteStudent(Long[] ids) {
        for (long id : ids) {
            studentRepository.deleteById(id);
        }
        return HttpStatus.OK;
    }


    @Override
    public ResponseEntity<Response> updateStudent(@NotNull UserDTO studentDTO) {
        User user = new User(
                studentDTO.getId(),
                studentDTO.getUsername(),
                passwordEncoder.encode(studentDTO.getPassword()),
                studentDTO.getFullName(),
                studentDTO.getRollNumber(),
                studentDTO.getGender(),
                studentDTO.getAddress(),
                studentDTO.getStatus(),
                studentDTO.getEmail(),
                studentDTO.getPhoneNumber()
        );
        Set<String> strRoles = studentDTO.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoleList(roles);
        studentRepository.save(user);
        return ResponseEntity.ok(new Response("", user));
    }

    private String generateRollNumber() {
        String lastRoll = studentRepository.getLastRollNumber().substring(2);
        int roll = Integer.parseInt(lastRoll.toString()) + 1;
        String pre = "HE";
        while (pre.length() + Integer.toString(roll).length() < 8) {
            pre += "0";
        }
        return pre + roll;
    }

}
