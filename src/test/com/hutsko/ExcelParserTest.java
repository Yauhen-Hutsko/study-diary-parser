package com.hutsko;

import com.hutsko.entity.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExcelParserTest {
    private static final String PATH_TEST_SHEET = "src/test/resources/testsheet.xlsx";
    private static final String SHEET_NAME = "Sheet1";

    private ExcelParser excelParser;
    private Sheet sheet;

    @BeforeEach
    void setUp() {
        excelParser = new ExcelParser(PATH_TEST_SHEET);
        sheet = excelParser.readSheet(SHEET_NAME);

    }

    @AfterEach
    void tearDown() {
        excelParser = null;
        sheet = null;
    }

    @Test
    public void testReadCorrectNumberOfLines() {
        List<Row> rows = excelParser.getRows(sheet);
        assertEquals(8, rows.size());
    }

    @Test
    public void testGetAllDays() {
        List<Row> rows = excelParser.getRows(sheet);
        Map<LocalDate, List<Row>> realDays = excelParser.getAllDays(rows);
        assertEquals(7, realDays.size());
    }

    @Test
    public void testParseDatePositive() {
        String time1 = "1,12,18";
        String time2 = "13,12,18";
        String time3 = "8,8,08";
        String time4 = "9,8,18";
        String time5 = "05,07,18";
        assertEquals(LocalDate.of(2018, 12, 1), excelParser.parseDate(time1));
        assertEquals(LocalDate.of(2018, 12, 13), excelParser.parseDate(time2));
        assertEquals(LocalDate.of(2008, 8, 8), excelParser.parseDate(time3));
        assertEquals(LocalDate.of(2018, 8, 9), excelParser.parseDate(time4));
        assertEquals(LocalDate.of(2018, 7, 5), excelParser.parseDate(time5));
        System.out.println(excelParser.parseDate(time2));
        System.out.println(excelParser.parseDate(time3).format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @Test
    public void testParseDateNegative() {
        String time1 = "1.12.18";
        String time2 = "1,12.18";
        String time3 = "1,12,2018";
        String time4 = "8,23,2018";
        assertThrows(DateTimeParseException.class, () -> assertEquals(LocalDate.of(2018, 12, 1), excelParser.parseDate(time1)));
        assertThrows(DateTimeParseException.class, () -> assertEquals(LocalDate.of(2018, 12, 1), excelParser.parseDate(time2)));
        assertThrows(DateTimeParseException.class, () -> assertEquals(LocalDate.of(2018, 12, 1), excelParser.parseDate(time3)));
        assertThrows(DateTimeParseException.class, () -> assertEquals(LocalDate.of(2018, 8, 23), excelParser.parseDate(time4)));
    }

    @Test
    public void testGetDurationPositive() {
        String one = "30m";
        String two = "4h15m";
        String three = "12h50m";
        String four = "3h";
        Duration dur1 = excelParser.getDuration(one);
        assertEquals("30m", dur1.toString());
        Duration dur2 = excelParser.getDuration(two);
        assertEquals("4h 15m", dur2.toString());
        Duration dur3 = excelParser.getDuration(three);
        assertEquals("12h 50m", dur3.toString());
        Duration dur4 = excelParser.getDuration(four);
        assertEquals("3h", dur4.toString());
    }

    public void testGetDurationNegative() {
//        1h, 1hour, 1 h, 1 hour
    }

    @Test
    public void testPrepareTimeFormatRU() {
        List<String> ruList = Arrays.asList(
                "10м", "10минут", "10мин", "20 м", "20 минут", "20 мин",
                "1ч", "2часа", "5часов", "1 час", "2 часа", "5 часов",
                "1ч15м", "1час15минут", "1 ч 15 м", "2 часа 15 мин", "5часов 15минут");

        List<String> expectedList = Arrays.asList(
                "10m", "10m", "10m", "20m", "20m", "20m",
                "1h", "2h", "5h", "1h", "2h", "5h",
                "1h15m", "1h15m", "1h15m", "2h15m", "5h15m");
        List<String> actualList = ruList.stream().map(s -> excelParser.prepareTimeFormat(s)).collect(Collectors.toList());
        ruList.forEach(s -> excelParser.prepareTimeFormat(s));

        assertEquals(expectedList, actualList);
    }

    @Test
    public void testPrepareTimeFormatEng() {
        List<String> engList = Arrays.asList(
                "10m", "10min", "10minutes", "10mins", "20 m", "20 min", "20 minutes", "20 mins",
                "1h", "2hours", "5hours", "1 hour", "2 hours",
                "1h15m", "1hour15minutes", "1 h 15 m", "2 hours 15 min", "5hours 15minutes");
        List<String> expectedList = Arrays.asList(
                "10m", "10m", "10m", "10m", "20m", "20m", "20m", "20m",
                "1h", "2h", "5h", "1h", "2h",
                "1h15m", "1h15m", "1h15m", "2h15m", "5h15m");
        List<String> actualList = engList.stream().map(s -> excelParser.prepareTimeFormat(s)).collect(Collectors.toList());
        engList.forEach(s -> excelParser.prepareTimeFormat(s));

        assertEquals(expectedList, actualList);
    }
}