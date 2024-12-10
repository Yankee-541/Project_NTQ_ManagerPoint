package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.MetadataExcelModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExportExcelDTO extends BaseExportExcelModel {

    private String fullName;
    private String rollNumber;
    private String email;
    private String groupClassDTO;
    private Double scoreDTO;
    private String subjectDTO;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "rollNumber", String.class, "Roll number")
                ,new MetadataExcelModel(2, "fullName", String.class, "Full name")
                ,new MetadataExcelModel(3, "email", String.class, "Email")
                ,new MetadataExcelModel(4, "groupClassDTO", GroupClass.class, "Class")
                ,new MetadataExcelModel(5, "scoreDTO", ScoreDTO.class, "Score")
                ,new MetadataExcelModel(6, "subjectDTO", SubjectDTO.class, "Subject")
        );
    }

    @Override
    public List<MetadataExcelModel> getListMetadataExcelModels() {
        return Arrays.asList(
                new MetadataExcelModel(1, "rollNumber", String.class, "Roll number")
                ,new MetadataExcelModel(2, "fullName", String.class, "Full name")
                ,new MetadataExcelModel(3, "email", String.class, "Email")
                ,new MetadataExcelModel(4, "subjectDTO", SubjectDTO.class, "Subject")
                ,new MetadataExcelModel(5, "scoreDTO", ScoreDTO.class, "Score")
        );
    }
}
