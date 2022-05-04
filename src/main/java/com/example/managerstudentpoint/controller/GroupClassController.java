package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.GroupClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/class")
@AllArgsConstructor
public class GroupClassController {
    private final GroupClassService groupClassService;
    @GetMapping("/{id}")
    public ResponseEntity<Response> detail(@PathVariable Long id) {
        return groupClassService.classById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> addClass(@RequestBody GroupClassDTO groupClassDTO){
        return groupClassService.addClass(groupClassDTO);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public HttpStatus deleteStudent(@RequestBody Long[] ids) {
        return groupClassService.deleteClass(ids);
    }
}
