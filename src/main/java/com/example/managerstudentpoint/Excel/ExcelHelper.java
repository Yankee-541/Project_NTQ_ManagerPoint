package com.example.managerstudentpoint.Excel;

import com.example.managerstudentpoint.entity.User;
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
    static String[] HEADERs = {"#","Id", "Roll number", "Full name", "User name", "Phone number", "Gender","Address", "Email"};
    static String SHEET = "Tutorials";


    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<User> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<User> tutorials = new ArrayList<User>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                User user = new User();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
//                        case 0:
//                            String id = String.valueOf(currentCell.getNumericCellValue());
//                            if (id != null) {
//                            user.setId((long) currentCell.getNumericCellValue());

//                            }
//                            break;
                        case 1:
                            user.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            user.setRollNumber(currentCell.getStringCellValue());
                            break;
                        case 3:
                            user.setFullName(currentCell.getStringCellValue());
                            break;
                        case 4:
                            user.setUsername(currentCell.getStringCellValue());
                            break;
                        case 5:
                            user.setPhoneNumber(currentCell.getStringCellValue());
                            break;
                        case 6:
                            user.setGender(currentCell.getStringCellValue());
                            break;
                        case 7:
                            user.setAddress(currentCell.getStringCellValue());
                            break;
                        case 8:
                            user.setEmail(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                tutorials.add(user);
            }
            workbook.close();
            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
