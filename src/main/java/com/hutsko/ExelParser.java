package com.hutsko;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class ExelParser {
    public static final String ROW_NAME = "программирование";
    private static final String PATH_FILE = "src/main/resources/ItLearnTimeWasted.xlsx";
    private static final String SHEET_NAME = "Schedule";

    public static void main(String[] args) {
        ExelParser ep = new ExelParser();
        Sheet sheet = ep.readSheet(SHEET_NAME);
        ep.parse(sheet);
    }

    private Sheet readSheet(String name) {
        Workbook wb = null;
        Sheet sheet = null;
        try (InputStream outStream = new FileInputStream(PATH_FILE)) {
            wb = new XSSFWorkbook(outStream);
            int sheetCounter = wb.getNumberOfSheets();
            sheet = wb.getSheet(name);
            int lastRow = sheet.getLastRowNum();
            System.out.println("sheetCounter: " + sheetCounter);
            System.out.println("lastRow: " + lastRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    private void parse(Sheet sheet) {
        List<Row> lines = null;
        Iterator<Row> rowIterator = sheet.rowIterator();
        StringBuilder result = new StringBuilder();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        result.append(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        result.append(cell.getNumericCellValue());
                        break;
                    case BLANK:
                        result.append("(-)");
                }
            }
            result.append("\n");
//            if (row.getCell(2).equals(ROW_NAME)) {
//                System.out.println(row.getCell(0));
//            }
        }
        System.out.println(result.toString());
    }
}
