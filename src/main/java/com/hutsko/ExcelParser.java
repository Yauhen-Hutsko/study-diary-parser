package com.hutsko;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
            System.out.println(" Last row number: " + lastRow);
            System.out.print("\033[0m");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }

    List<Row> parse(Sheet sheet) {
        List<Row> rows = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            rows.add(row);

            StringBuilder currentLine = new StringBuilder();
            while (cellIterator.hasNext()) {
                processCell(cellIterator, currentLine);
            }

//            result.append("\n");
//            if (row.getCell(2).equals(ROW_NAME)) {
//                System.out.println(row.getCell(0));
//            }
        }
        return rows;
    }

    Map<String, List<Row>> getAllDays(List<Row> rowList) {
        String previousDay = "";
        Map<String, List<Row>> days = new HashMap<>();
        List<Row> activities = new ArrayList<>();

        for (int i = 0; i < rowList.size(); i++) {
            Row currentRow = rowList.get(i);
            previousDay = currentRow.getCell(0) != null ? currentRow.getCell(0).toString() : previousDay;

            if (currentRow.getCell(1) != null) {
                activities.clear();
            }
            activities.add(currentRow);
            days.put(previousDay, activities);
        }
        return days;
    }

    void processCell(Iterator<Cell> cellIterator, StringBuilder currentLine) {
        Cell cell = cellIterator.next();
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case BLANK:
                currentLine.append("(-)");
                break;
            case STRING:
                currentLine.append(cell.getStringCellValue());
                break;
            case NUMERIC:
                currentLine.append(cell.getNumericCellValue());
                break;
        }
    }

    LocalDate parseDate(String dateString) throws DateTimeParseException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("d,M,yy");
        //The returned date has the format "yyyy-MM-dd" (ISO_DATE)
        return LocalDate.parse(dateString, format);
    }

    Duration getDuration(String string){
     return null;
    }

    String prepareTimeFormat(String rawDuration){
        String minutePatternEng = "m[a-zA-Z]{2,6}+|м[а-яА-Я]{0,4}+";
        String hourPatternEng = "h[a-zA-Z]{3,4}+|ч[а-яА-Я]{0,4}+";
        // delete spaces from string
        String duration = rawDuration.replace(" ", "").trim();
        // truncate words to "h" and "m"
        duration = duration.replaceAll(minutePatternEng, "m");
        duration = duration.replaceAll(hourPatternEng, "h");

        // replace all russian symbols by english "h" and "m"
    return duration;
    }
}
