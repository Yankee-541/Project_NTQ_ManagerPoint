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
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.AuthenService;
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

    private final UserRepository userRepository;
    private final ObjectMapper objectmapper;
    private final AuthenticationManager authenticationMana;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public ResponseEntity<Response> signup(UserDTO signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Username is exist !!!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Email is exist"));
        }
        User user = new User(
                signUpRequest.getUsername(),
//                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFullName(),
                generateRollNumber(),
                signUpRequest.getGender(),
                signUpRequest.getAddress(),
                signUpRequest.getEmail(),
                signUpRequest.getPhoneNumber()
        );
        user.setIsDelete(false);
        user.setPassword(passwordEncoder.encode("123456"));
        GroupClass groupclass = objectmapper.convertValue(signUpRequest.getGroupClass(), GroupClass.class);

        user.setGroupClass(groupclass);

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
                    case "student":
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoleList(roles);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequestDTO loginRequest) throws NoSuchAlgorithmException {
        if (userRepository.existsByUsername(loginRequest.getUsername())) {
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
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles
            ));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse("Username or password is wrong or invalid"));
        }
    }

    @Override
    public boolean login_1(UserDTO loginRequest) {
        if (userRepository.existsByUsername(loginRequest.getUsername())) {
            Authentication authentication = authenticationMana.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserById(Long userId) {
        User user = (User) objectmapper.convertValue(userRepository.findById(userId).orElse(null),
                UserRepository.class);
        return (UserDetails) user;
    }

    @Override
    public ResponseEntity<Response> addAccStudent(UserDTO userDTO) {
        if (!userRepository.existsByUsername(userDTO.getUsername())) {
            if (!userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
                if (!userRepository.existsByEmail(userDTO.getEmail())) {
                    userDTO.setRollNumber(generateRollNumber());
                    userRepository.save(objectmapper.convertValue(userDTO, User.class));
                    return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.BAD_REQUEST));
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.BAD_REQUEST));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.BAD_REQUEST)
            );
        }
    }

    @Override
    public ResponseEntity<Response> deleteStudent(Long[] ids) {
        for (long id : ids) {
            User user = userRepository.findByIdAndIsDelete(id, false).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
            }
            user.setIsDelete(true);
            userRepository.save(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response("Delete successful!"));
    }

    @Override
    public ResponseEntity<Response> updateStudent(@NotNull UserDTO userDTO) {
        if (!userRepository.existsByUsernameAndIsDeleteAndRollNumber(userDTO.getUsername(), false, userDTO.getRollNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));
        }
        if (userDTO.getGender().equalsIgnoreCase("male") || userDTO.getGender().equalsIgnoreCase("female")) {
            User user = new User(
                    userDTO.getId(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    userDTO.getFullName(),
                    userDTO.getRollNumber(),
                    userDTO.getGender(),
                    userDTO.getAddress(),
                    userDTO.getEmail(),
                    userDTO.getPhoneNumber()
            );
            user.setUsername(userDTO.getUsername());
            user.setIsDelete(false);
            GroupClass groupclass = objectmapper.convertValue(userDTO.getGroupClass(), GroupClass.class);
            user.setGroupClass(groupclass);


            Set<String> strRoles = userDTO.getRole();
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
            userRepository.save(user);
            return ResponseEntity.ok(new Response(HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));
    }

    @Override
    public ResponseEntity<Response> changePassword(UserDTO userDTO, String newPass) throws NoSuchAlgorithmException {
        if (login_1(userDTO)) {
            User user = objectmapper.convertValue(userRepository.findById(userDTO.getId()).orElse(null),
                    User.class);
            user.setId(userDTO.getId());
            user.setUsername(user.getUsername());
            user.setEmail(user.getEmail());
            user.setGender(user.getGender());
            user.setRollNumber(user.getRollNumber());
            user.setAddress(user.getAddress());
            user.setFullName(user.getFullName());
            user.setPhoneNumber(user.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(newPass));

            GroupClass groupclass = objectmapper.convertValue(user.getGroupClass(), GroupClass.class);
            user.setGroupClass(groupclass);

            Set<String> strRoles = userDTO.getRole();
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
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(HttpStatus.OK)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Response(HttpStatus.BAD_REQUEST)
            );
        }
    }

    public String generateRollNumber() {
        String lastRoll = userRepository.getLastRollNumber().substring(2);
        int roll = Integer.parseInt(lastRoll.toString()) + 1;
        String pre = "HE";
        while (pre.length() + Integer.toString(roll).length() < 8) {
            pre += "0";
        }
        return pre + roll;
    }

}
