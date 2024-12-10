package com.example.managerstudentpoint.service.Impl;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.dto.InfoClassDTO;
import com.example.managerstudentpoint.dto.InfoStudentDTO;
import com.example.managerstudentpoint.dto.InfoSubjectDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GroupClassServiceImpl implements GroupClassService {
    private final ObjectMapper objectMapper;
    private final GroupClassRepository classRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Response> listClass(String key) {
        List<GroupClass> groupClasses = classRepository.getGroupClassByClassName(false, key);
        if (groupClasses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        } else {
            List<InfoClassDTO> groupClassDTOS = new ArrayList<>();
            for (GroupClass groupClass : groupClasses) {
                groupClassDTOS.add(objectMapper.convertValue(groupClass, InfoClassDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(groupClassDTOS)
            );
        }
    }

    @Override
    public ResponseEntity<Response> getStudentByClassId(String groupClass) {

        List<User> users = classRepository.findAllStudentByClassName(false, groupClass);

        if (users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
        }else {
            List<InfoStudentDTO> infoStudentDTOS = new ArrayList<>();
            for (User user : users) {
                InfoClassDTO infoClassDTO = new InfoClassDTO();
                InfoStudentDTO infoStudentDTO = objectMapper.convertValue(infoClassDTO.getUsers(), InfoStudentDTO.class);
                infoClassDTO.setUsers(infoStudentDTO);
                infoStudentDTOS.add(objectMapper.convertValue(user, InfoStudentDTO.class));
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(infoStudentDTOS)
            );

        }
//        GroupClassDTO groupClassDTO = objectMapper.convertValue(classRepository.findById(id).orElse(null), GroupClassDTO.class);
//        if (groupClassDTO == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new Response(HttpStatus.NOT_FOUND)
//            );
//        } else {
//            List<User> userList = userRepository.findAllByGroupClass(new GroupClass(id));
//            if (!userList.isEmpty()) {
//                groupClassDTO.setUserList(userList);
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new Response(groupClassDTO)
//                );
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Don't have student in this class"));
//        }
    }

    @Override
    public ResponseEntity<Response> deleteClass(Long[] ids) {
        for (long id : ids) {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(HttpStatus.BAD_REQUEST));

            }
            GroupClass groupClass = classRepository.findById(id).orElse(null);
            if (groupClass != null && !groupClass.getStatus()) {
                groupClass.setStatus(true);
                classRepository.save(groupClass);
                return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND));
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Response> updateClass(GroupClassDTO groupClassDTO) {
        GroupClass groupClass = classRepository.findById(groupClassDTO.getId()).orElse(null);
        if (groupClass == null || groupClass.getStatus()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response(HttpStatus.NOT_FOUND)
            );
        } else {
            groupClassDTO.setStatus(groupClass.getStatus());
            classRepository.save(objectMapper.convertValue(groupClassDTO, GroupClass.class));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new Response(groupClassDTO)
            );
        }

    }

    @Override
    public ResponseEntity<Response> addClass(GroupClassDTO groupClassDTO) {
        if (classRepository.existsByClassName(groupClassDTO.getClassName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new Response("Name class is exist!!!"));
        }
        GroupClass groupClass = new GroupClass(
                groupClassDTO.getClassName()
        );
        groupClass.setStatus(false);
        classRepository.save(groupClass);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(groupClassDTO));
    }
}
