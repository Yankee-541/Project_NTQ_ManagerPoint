package com.example.managerstudentpoint.controller;

import com.example.managerstudentpoint.dto.ScoreDTO;
import com.example.managerstudentpoint.dto.StudentExportExcelDTO;
import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import com.example.managerstudentpoint.response.Response;
import com.example.managerstudentpoint.service.Impl.XLSXFileServiceImpl;
import com.example.managerstudentpoint.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/student")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @Autowired
    XLSXFileServiceImpl exportExcelFileService;

    @GetMapping("/ScoreBySubject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getScoreSubject(
            @RequestParam(name = "key", defaultValue = "") Long key,
            @RequestParam(name = "size", defaultValue = "5") Integer pageSize,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        if (page <= 0) {
            page = 1;
        }
        return scoreService.getScoresByClassAndCourse(key, page, pageSize);
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportScoreBySubjectAndClass(@Validated @RequestParam(name = "subId") Long sub_id,
                                             @RequestParam(name = "cId") Long c_id) throws IOException {
        List<BaseExportExcelModel> list = new ArrayList<>();
        for (StudentExportExcelDTO user : scoreService.getScoreBySubjectAndClass(sub_id, c_id)) {
            list.add(user);
        }
        exportExcelFileService.exportFile(
                "test",
                "repost student",
                list, StudentExportExcelDTO.class);
    }

    @PostMapping("/addScore")
    public ResponseEntity<Response> addScoreForStudent(@Validated @RequestBody ScoreDTO scoreDTO){
        return scoreService.addScoreforStudent(scoreDTO);
    }

    @PutMapping("/updateScore")
    public ResponseEntity<Response> updateScoreForStudent(@Validated @RequestBody ScoreDTO scoreDTO){
        return scoreService.updateScoreforStudent(scoreDTO);
    }

}
