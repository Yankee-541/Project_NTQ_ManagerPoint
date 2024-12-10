package com.example.managerstudentpoint.Excel;

import com.example.managerstudentpoint.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class StudentExportExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<User> userList;

    public StudentExportExcel(List<User> userList) {
        this.userList = userList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "User ID", style);
        createCell(row, 1, "Full name", style);
        createCell(row, 2, "User name", style);
        createCell(row, 3, "Password", style);
        createCell(row, 4, "Phone number", style);
        createCell(row, 5, "Email", style);
        createCell(row, 6, "Address", style);
        createCell(row, 7, "Roles", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (User user : userList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.getRollNumber(), style);
            createCell(row, columnCount++, user.getFullName(), style);
            createCell(row, columnCount++, user.getUsername(), style);
            createCell(row, columnCount++, user.getPassword(), style);
            createCell(row, columnCount++, user.getPhoneNumber(), style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getAddress(), style);
            createCell(row, columnCount++, user.getRoleList().toString(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

}
