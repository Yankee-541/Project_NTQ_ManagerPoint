package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.GroupClassRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.GroupClassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GroupClassServiceImpl implements GroupClassService {
    private final ObjectMapper objectMapper;
    private final GroupClassRepository classRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Response> classById(Long id) {
        GroupClassDTO groupClassDTO = objectMapper.convertValue(classRepository.findById(id).orElse(null), GroupClassDTO.class);
        if (groupClassDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response("Don't have class with id: " + id, "")
            );
        } else {
            List<User> userList = userRepository.findAllByGroupClass(new GroupClass(id, null));
            if (!userList.isEmpty()) {
                groupClassDTO.setUserList(userList);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new Response("Found class have class_id: " + id, groupClassDTO)
                );
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response("Found class have class_id: " + id, groupClassDTO)
            );

        }
    }

    @Override
    public ResponseEntity<String> deleteClass(Long[] ids) {
        for (long id : ids) {
            GroupClass groupClass = classRepository.findById(id).orElse(null);
            if(groupClass == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found class with id ");
            }
            groupClass.setStatus(true);
            classRepository.save(groupClass);
        }
        return ResponseEntity.ok("Delete class successful!");
    }

    @Override
    public ResponseEntity<Response> addClass(GroupClassDTO groupClassDTO) {
        if (classRepository.existsByClassName(groupClassDTO.getClassName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Error: Group class is already taken!", ""));
        }
        GroupClass groupClass = new GroupClass(
                groupClassDTO.getClassName(),
                groupClassDTO.getStatus()
        );
        classRepository.save(groupClass);

        return ResponseEntity
                .badRequest()
                .body(new Response("", groupClassDTO));
    }
}
