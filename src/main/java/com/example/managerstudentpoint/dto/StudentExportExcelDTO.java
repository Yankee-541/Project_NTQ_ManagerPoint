package com.example.managerstudentpoint.dto;

import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.MetadataExcelModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExportExcelDTO extends BaseExportExcelModel {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String rollNumber;
    private String gender;
    private String address;
    private String email;
    private String phoneNumber;

    @Override
    public List<MetadataExcelModel> getListMetadata() {
        return Arrays.asList(
                new MetadataExcelModel(1, "id", Long.class, "Mã học sinh"),
                new MetadataExcelModel(2, "rollNumber", String.class, "Roll number"),
                new MetadataExcelModel(3, "fullName", String.class, "Full name"),
                new MetadataExcelModel(4, "username", String.class, "User name"),
                new MetadataExcelModel(5, "phoneNumber", String.class, "Phone number"),
                new MetadataExcelModel(6, "gender", String.class, "Gender"),
                new MetadataExcelModel(7, "address", String.class, "Address"),
                new MetadataExcelModel(8, "email", String.class, "Email")
//                ,new MetadataExcelModel(9, "groupClass", GroupClass.class, "Class")
        );
    }
}
