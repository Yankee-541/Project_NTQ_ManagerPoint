package com.example.managerstudentpoint.service.Impl;


import com.example.managerstudentpoint.Excel.ExcelHelper;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.entity.BaseExportExcelModel;
import com.example.managerstudentpoint.entity.GroupClass;
import com.example.managerstudentpoint.entity.MetadataExcelModel;
import com.example.managerstudentpoint.entity.User;
import com.example.managerstudentpoint.repository.GroupClassRepository;
import com.example.managerstudentpoint.repository.ScoreRepository;
import com.example.managerstudentpoint.repository.SubjectRepository;
import com.example.managerstudentpoint.repository.UserRepository;
import com.example.managerstudentpoint.service.ExcelFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class XLSXFileServiceImpl implements ExcelFileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupClassRepository classRepository;

    @Autowired
    AuthenServiceImpl authenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public File exportFile(String fileName,
                           String sheetName,
                           List<BaseExportExcelModel> dataExport,
                           Class<? extends BaseExportExcelModel> classType) {
        File excelFile = null;
        try {
            Path pathExcelFile = Files.createTempFile(Paths.get("C:\\Users\\dang.tran2\\Downloads"), fileName, ".xlsx");
            excelFile = pathExcelFile.toFile();

            POIXMLDocument workbook = exportExcel(dataExport, classType, sheetName);

            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelFile != null && excelFile.exists()) {
            }
        }
        return null;
    }

    @Override
    public File exportScoreByRollnumber(String fileName,
                                        String sheetName,
                                        List<BaseExportExcelModel> dataExport,
                                        Class<? extends BaseExportExcelModel> classType) {
        File excelFile = null;
        try {
            Path pathExcelFile = Files.createTempFile(Paths.get("C:\\Users\\dang.tran2\\Downloads"), fileName, ".xlsx");
            excelFile = pathExcelFile.toFile();

            POIXMLDocument workbook = exportScoreByRollnumber(dataExport, classType, sheetName);

            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelFile != null && excelFile.exists()) {
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<String> importStudents(MultipartFile file) {
        try {
            List<UserDTO> getData = ExcelHelper.readStudentExcelFile(file.getInputStream());
            if (!getData.isEmpty()) {
                for (UserDTO userDTO : getData) {
                    User user = objectMapper.convertValue(userDTO, User.class);
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    user.setRollNumber(authenService.generateRollNumber());
                    GroupClass clazz = classRepository.findByClassName(userDTO.getGroupClass().getClassName());
                    user.setGroupClass(clazz);
                    userRepository.save(user);
                }
                return ResponseEntity.status(200).body("Import Successful!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error! Please try again.");
        }
        return ResponseEntity.status(404).body("File is empty! Check again.");
    }

    public <T> POIXMLDocument exportExcel(List<BaseExportExcelModel> listData,
                                          Class<? extends BaseExportExcelModel> classType,
                                          String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            Constructor<? extends BaseExportExcelModel> ctor = classType.getConstructor();
            BaseExportExcelModel object = ctor.newInstance();
            List<String> headers = object.getHeaders();
            List<MetadataExcelModel> listMetadata = object.getListMetadata();
            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers.get(col));
            }
            AtomicInteger rowIdx = new AtomicInteger(1);
            listData.forEach(item -> {
                int index = rowIdx.getAndIncrement();
                Row row = sheet.createRow(index);
                row.createCell(0).setCellValue(index);
                for (MetadataExcelModel info : listMetadata) {
                    try {
                        String fieldName = info.getFieldName();
                        Method methodGet = classType.getDeclaredMethod(
                                "get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1)
                        );
                        Object valueObject = methodGet.invoke(item);
                        String value = "";
                        if (valueObject != null) {
                            // TODO: Need modify when has other type need convert
                            switch (info.getParameterType().getName()) {
                                case "java.time.LocalDate":
                                    value = DATE_FORMATTER.format(((LocalDate) valueObject));
                                    break;
                                case "java.time.LocalDateTime":
                                    value = DATE_TIME_FORMATTER.format(((LocalDateTime) valueObject));
                                    break;
                                case "java.lang.String":
                                default:
                                    value = valueObject.toString();
                            }
                        }
                        row.createCell(info.getPosition()).setCellValue(value);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    public <T> POIXMLDocument exportScoreByRollnumber(List<BaseExportExcelModel> listData,
                                          Class<? extends BaseExportExcelModel> classType,
                                          String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            Constructor<? extends BaseExportExcelModel> ctor = classType.getConstructor();
            BaseExportExcelModel object = ctor.newInstance();
            List<String> headers = object.getHeaderForScoreByRollnumber();
            List<MetadataExcelModel> listMetadata = object.getListMetadataExcelModels();
            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.size(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers.get(col));
            }
            AtomicInteger rowIdx = new AtomicInteger(1);
            listData.forEach(item -> {
                int index = rowIdx.getAndIncrement();
                Row row = sheet.createRow(index);
                row.createCell(0).setCellValue(index);
                for (MetadataExcelModel info : listMetadata) {
                    try {
                        String fieldName = info.getFieldName();
                        Method methodGet = classType.getDeclaredMethod(
                                "get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1)
                        );
                        Object valueObject = methodGet.invoke(item);
                        String value = "";
                        if (valueObject != null) {
                            // TODO: Need modify when has other type need convert
                            switch (info.getParameterType().getName()) {
                                case "java.time.LocalDate":
                                    value = DATE_FORMATTER.format(((LocalDate) valueObject));
                                    break;
                                case "java.time.LocalDateTime":
                                    value = DATE_TIME_FORMATTER.format(((LocalDateTime) valueObject));
                                    break;
                                case "java.lang.String":
                                default:
                                    value = valueObject.toString();
                            }
                        }
                        row.createCell(info.getPosition()).setCellValue(value);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }
}
