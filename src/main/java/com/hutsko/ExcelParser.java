package com.hutsko;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser {
    public static final String ROW_NAME = "программирование";
    private static final String PATH_FILE = "src/main/resources/ItLearnTimeWasted.xlsx";
    private static final String SHEET_NAME = "Schedule";

    private String pathFile;

    public ExcelParser(String pathFile) {
        this.pathFile = pathFile;
    }

    public static void main(String[] args) {
        ExcelParser ep = new ExcelParser(PATH_FILE);
        Sheet sheet = ep.readSheet(SHEET_NAME);
        ep.parse(sheet);
    }

    Sheet readSheet(String name) {
        Workbook wb = null;
        Sheet sheet = null;
        try (InputStream outStream = new FileInputStream(pathFile)) {
            wb = new XSSFWorkbook(outStream);
            int sheetCounter = wb.getNumberOfSheets();
            sheet = wb.getSheet(name);
            int lastRow = sheet.getLastRowNum();
            System.out.println("\033[33m\033[1m sheetCounter: " + sheetCounter);
            System.out.println(" lastRow: " + lastRow);
            System.out.print("\033[0m");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    List<String> parse(Sheet sheet) {
        List<String> lines = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
//        StringBuilder result = new StringBuilder();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            StringBuilder currentLine = new StringBuilder();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case BLANK:
//                        result.append("(-)");
                        currentLine.append("(-)");
                        break;
                    case STRING:
//                        result.append(cell.getStringCellValue());
                        currentLine.append(cell.getStringCellValue());
                        break;
                    case NUMERIC:
//                        result.append(cell.getNumericCellValue());
                        currentLine.append(cell.getNumericCellValue());
                        break;
                }
            }
            lines.add(currentLine.toString());

//            result.append("\n");
//            if (row.getCell(2).equals(ROW_NAME)) {
//                System.out.println(row.getCell(0));
//            }
        }
//        System.out.println(result.toString());
        return lines;
    }
}
