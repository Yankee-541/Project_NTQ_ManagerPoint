package com.example.managerstudentpoint.JwtUtil;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashpass {
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();

//        return passwordEncoder.encode(password);
    }
}
