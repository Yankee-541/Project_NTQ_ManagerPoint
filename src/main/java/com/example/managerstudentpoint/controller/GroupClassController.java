package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.GroupClassService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/class")
public class GroupClassController {

    @Autowired
    GroupClassService groupClassService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllClass(
            @RequestParam(name = "key", defaultValue = "") String key
    ){
        return groupClassService.listClass(key);
    }

    @GetMapping("/{groupClass}/student")
    public ResponseEntity<Response> getStudentByClassId(@PathVariable String groupClass) {
        return groupClassService.getStudentByClassId(groupClass);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addClass(@RequestBody GroupClassDTO groupClassDTO) {
        return groupClassService.addClass(groupClassDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteClass(@RequestBody Long[] ids) {
        return groupClassService.deleteClass(ids);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateClass(@RequestBody GroupClassDTO groupClassDTO) {
        return groupClassService.updateClass(groupClassDTO);
    }

}
