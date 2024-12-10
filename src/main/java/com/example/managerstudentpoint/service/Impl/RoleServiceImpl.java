package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.RoleDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.Role;
import com.example.managerstudentpoint.repository.RoleRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;

@Service
@AllArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
//    @Override
//    public ResponseEntity<?> addRole(RoleDTO roleDTO){
//        Role role = new Role(roleDTO.getName());
//        return roleRepository.save(roleDTO);
//    }
}
