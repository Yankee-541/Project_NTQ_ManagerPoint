package com.example.managerstudentpoint.Excel;

import com.example.managerstudentpoint.dto.GroupClassDTO;
import com.example.managerstudentpoint.dto.UserDTO;
import com.example.managerstudentpoint.service.Impl.AuthenServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"User name",
            "Password",
            "Full name",
            "Phone number",
            "Email",
            "Gender",
            "Address",
            "GroupClass"};
    static String SHEET = "Student";

    AuthenServiceImpl authenService;

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<UserDTO> readStudentExcelFile(InputStream is) {
        List<UserDTO> userDTOlist = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(is);
            for (Sheet sheet : workbook) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    if (rowNumber == 0) {
                        rowNumber++;
                        continue;
                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    int cellIdx = 0;
                    UserDTO user = new UserDTO();
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        switch (cellIdx) {
                            case 0:
                                user.setUsername(currentCell.getStringCellValue());
                                break;
                            case 1:
                                user.setPassword(currentCell.getStringCellValue());
                                break;
                            case 2:
                                user.setFullName(currentCell.getStringCellValue());
                                break;
                            case 3:
                                user.setPhoneNumber(String.valueOf(currentCell.getNumericCellValue()));
                                break;
                            case 4:
                                user.setEmail(currentCell.getStringCellValue());
                                break;
                            case 5:
                                user.setGender(currentCell.getStringCellValue());
                                break;
                            case 6:
                                user.setAddress(currentCell.getStringCellValue());
                                break;
                            case 7:
                                GroupClassDTO groupClassDTO = new GroupClassDTO();
                                groupClassDTO.setClassName(currentCell.getStringCellValue());
                                user.setGroupClass(groupClassDTO);
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    userDTOlist.add(user);
                }
//                workbook.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
        return userDTOlist;
    }
}
