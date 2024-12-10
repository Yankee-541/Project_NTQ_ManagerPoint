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

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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
    public ResponseEntity<?> signup(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username is exist !!!");
        }
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Phone number is exist");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is exist");
        }
        User user = new User(userDTO.getUsername(), userDTO.getFullName(), generateRollNumber(), userDTO.getGender(), userDTO.getAddress(), userDTO.getEmail(), userDTO.getPhoneNumber());
        user.setIsDelete(false);
        user.setPassword(passwordEncoder.encode("123456"));
        GroupClass groupclass = objectmapper.convertValue(userDTO.getGroupClass(), GroupClass.class);

        user.setGroupClass(groupclass);
        Set<Role> roles = chooseRoleForStudent(userDTO);
        user.setRoleList(roles);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @Override
    public ResponseEntity<?> login(LoginRequestDTO loginRequest) {
        if (userRepository.existsByUsername(loginRequest.getUsername())) {
            Authentication authentication = authenticationMana.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), roles));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username is wrong or not found");
        }
    }

    @Override
    public ResponseEntity<Response> resetPassword(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            User user = userRepository.getUserByUsername(userDTO.getUsername());
            setAgainForStudentInUpdate(user);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
        }
    }

    @Override
    public ResponseEntity<?> forgotPasswordSendToMail(UserDTO userDTO) throws MessagingException, IOException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            sendmail(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
        }
    }

    private void sendmail(UserDTO userDTO) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("dangtdhe150020@fpt.edu.vn", "dang050401");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(userDTO.getEmail(), false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userDTO.getEmail()));
        msg.setSubject("Reset passowrd");
//        msg.setContent("Tutorials point email 2", "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Password: \'" + generatePassword() + "\'. Link login: http://localhost:5999/api/auth/login", "text/html");

        //take inf student and after update
        User user = userRepository.getUserByEmail(userDTO.getEmail());
        setAgainForStudentInUpdate(user);
        userRepository.save(user);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
//        MimeBodyPart attachPart = new MimeBodyPart();

//        attachPart.attachFile("/img/1M6A2678.JPG");
//        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        Transport.send(msg);
    }

    public void setAgainForStudentInUpdate(User user) {
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEmail(user.getEmail());
        user.setGender(user.getGender());
        user.setRollNumber(user.getRollNumber());
        user.setAddress(user.getAddress());
        user.setFullName(user.getFullName());
        user.setPhoneNumber(user.getPhoneNumber());
        user.setRoleList(user.getRoleList());

        GroupClass groupclass = objectmapper.convertValue(user.getGroupClass(), GroupClass.class);
        user.setGroupClass(groupclass);
    }


    @Override
    public boolean login_1(UserDTO loginRequest) {
        if (userRepository.existsByUsername(loginRequest.getUsername())) {
            Authentication authentication = authenticationMana.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserById(Long userId) {
        User user = (User) objectmapper.convertValue(userRepository.findById(userId).orElse(null), UserRepository.class);
        return (UserDetails) user;
    }

    @Override
    public ResponseEntity<?> deleteStudent(Long[] ids) {
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
            User user = new User(userDTO.getId(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFullName(), userDTO.getRollNumber(), userDTO.getGender(), userDTO.getAddress(), userDTO.getEmail(), userDTO.getPhoneNumber());
            user.setUsername(userDTO.getUsername());
            user.setIsDelete(false);
            GroupClass groupclass = objectmapper.convertValue(userDTO.getGroupClass(), GroupClass.class);
            user.setGroupClass(groupclass);
            Set<Role> roles = chooseRoleForStudent(userDTO);
            user.setRoleList(roles);
            userRepository.save(user);
            return ResponseEntity.ok(new Response(HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));
    }

    public Set<Role> chooseRoleForStudent(UserDTO userDTO) {
        Set<String> strRoles = userDTO.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }

    @Override
    public ResponseEntity<Response> changePassword(UserDTO userDTO, String newPass) throws NoSuchAlgorithmException {
        if (login_1(userDTO)) {
            User user = objectmapper.convertValue(userRepository.findById(userDTO.getId()).orElse(null), User.class);
            setAgainForStudentInUpdate(user);

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));
        }
    }

    public StringBuilder generatePassword() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb;
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
