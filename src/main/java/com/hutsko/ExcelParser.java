package com.hutsko;

import com.hutsko.entity.Activity;
import com.hutsko.entity.ActivityType;
import com.hutsko.entity.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelParser {
    public static final String ROW_NAME = "программирование";
    static final String PATH_FILE = "src/main/resources/ItLearnTimeWasted.xlsx";
    static final String SHEET_NAME = "Schedule";
    private static final int DATE_CELL_NUMBER = 0;
    private static final int ACTIVITY_CELL_NUMBER = 2;
    private static final int TIME_CELL_NUMBER = 5;
    private static int rowCounter;
    private String pathFile;

    public ExcelParser(String pathFile) {
        this.pathFile = pathFile;
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

    /**
     * This is a main method which return data prepared for UI.
     * Some days contain multi-tuple of activities. Therefore "unique days" ≤ "max lines"
     *
     * @return HashMap where key - day, value - list of activities.
     */
    Map<LocalDate, List<Activity>> getAllDays(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        Map<LocalDate, List<Activity>> days = new HashMap<>();
        List<Activity> activities = new ArrayList<>();
        LocalDate previousDay = LocalDate.now();

        try {
            while (rowIterator.hasNext()) {
                rowCounter++;
                Row currentRow = rowIterator.next();
                previousDay = currentRow.getCell(DATE_CELL_NUMBER) != null ?
                        parseDate(currentRow.getCell(DATE_CELL_NUMBER).toString()) : previousDay;
                // in this case "activity" will be placed under a new day. Otherwise to the previous one.
                if (currentRow.getCell(DATE_CELL_NUMBER) != null) {
                    activities = new ArrayList<>();
                }
                if (currentRow.getCell(ACTIVITY_CELL_NUMBER) != null && currentRow.getCell(TIME_CELL_NUMBER) != null &&
                        !currentRow.getCell(TIME_CELL_NUMBER).toString().equals("0")) {
                    //populate entities with data from the cells.
                    activities.add(getActivity(currentRow));
                    days.put(previousDay, activities);
                }
            }
        } catch (Exception e) {
            System.out.println("Error at line: " + rowCounter);
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return days;
    }

    @Deprecated
    private void processCell(Iterator<Cell> cellIterator, StringBuilder currentLine) {
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

    String prepareTimeFormat(String rawDuration) {
        String minutePatternEng = "m[a-zA-Z]{2,6}+|м[а-яА-Я]{0,4}+";
        String hourPatternEng = "h[a-zA-Z]{3,4}+|ч[а-яА-Я]{0,4}+";
        // delete spaces from string
        String duration = rawDuration.replace(" ", "").trim();
        // change data format like "{"1час 15 мин"} -> "1h15m"
        duration = duration.replaceAll(minutePatternEng, "m");
        duration = duration.replaceAll(hourPatternEng, "h");
        if (!duration.contains("h") && !duration.contains("m")) {
            return "0h0m";
        }
        if (!duration.contains("h")) {
            duration = "0h" + duration;
        }
        if (!duration.contains("m")) {
            duration = duration + "0m";
        }
        return duration;
    }

    Duration parseDuration(String dur) {
        //check for correct input format like "XXhYYm"
        if (!dur.matches("\\d{1,2}h[0-5]?\\d{1}m")) {
            throw new IllegalArgumentException("Wrong input string format: " + dur);
        }

        //Catch up to 2 first digits in string like "10h30m"
        Pattern hourPattern = Pattern.compile("\\d+(?=h)");
        //Catch up to 2 digits between "h" and "m" in string like "10h30m"
        Pattern minutePattern = Pattern.compile("\\d+(?=m)");
        Matcher hourMatcher = hourPattern.matcher(dur);
        Matcher minuteMatcher = minutePattern.matcher(dur);

        int hour = (hourMatcher.find()) ? Integer.parseInt(dur.substring(hourMatcher.start(), hourMatcher.end())) : 0;
        int minute = (minuteMatcher.find()) ? Integer.parseInt(dur.substring(minuteMatcher.start(), minuteMatcher.end())) : 0;

        return new Duration(hour, minute);
    }

    Activity getActivity(Row row) {
        String name = row.getCell(ACTIVITY_CELL_NUMBER).toString().trim();

        //this check will throw an exception in case of unknown activity
        ActivityType.forString(name);

        Duration time = parseDuration(prepareTimeFormat(row.getCell(TIME_CELL_NUMBER).toString()));
        return new Activity(name, time);
    }

    int getRowCounter() {
        return rowCounter;
    }
}
