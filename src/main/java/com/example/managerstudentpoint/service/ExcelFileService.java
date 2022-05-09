package com.example.managerstudentpoint.service;

import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface ExcelFileService {
    DateTimeFormatter DATE_FORMATTER =  DateTimeFormatter.ofPattern("dd/MM/yyyy");

    DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    File exportFile(String fileName, String sheetName, List<BaseExportExcelModel> dataExport, Class<? extends BaseExportExcelModel> classType);

    File exportScoreByRollnumber(String fileName, String sheetName, List<BaseExportExcelModel> dataExport, Class<? extends BaseExportExcelModel> classType);

    ResponseEntity<String> importStudents(MultipartFile file);



}