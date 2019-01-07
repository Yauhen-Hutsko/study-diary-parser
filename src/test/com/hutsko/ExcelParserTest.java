package com.hutsko;

import com.hutsko.entity.Activity;
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

import static org.junit.jupiter.api.Assertions.*;

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
        excelParser.getAllDays(sheet);
        int num = excelParser.getRowCounter();
        assertEquals(8, num);
    }
    @Test
    public void testGetAllDays() {
        Map<LocalDate, List<Activity>> realDays = excelParser.getAllDays(sheet);
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
    public void testParseDurationPositive() {
        String one = "0h30m";
        String two = "4h15m";
        String three = "12h50m";
        String four = "3h0m";
        assertEquals("30m", excelParser.parseDuration(one).toString());
        assertEquals("4h 15m", excelParser.parseDuration(two).toString());
        assertEquals("12h 50m", excelParser.parseDuration(three).toString());
        assertEquals("3h", excelParser.parseDuration(four).toString());
    }
    @Test
    public void testParseDurationNegative() {
        String one = "3hour";
        String two = "1h85m";
        String three = "3u";
        String four = "3ч";
        String five = "2h20h";
        String six = "4m40m";
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(one));
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(two));
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(three));
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(four));
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(five));
        assertThrows(IllegalArgumentException.class,() -> excelParser.parseDuration(six));

    }
    @Test
    public void testPrepareTimeFormatRU() {
        List<String> ruList = Arrays.asList(
                "10м", "10минут", "10мин", "20 м", "20 минут", "20 мин",
                "1ч", "2часа", "5часов", "1 час", "2 часа", "5 часов",
                "1ч15м", "1час15минут", "1 ч 15 м", "2 часа 15 мин", "5часов 15минут");

        List<String> expectedList = Arrays.asList(
                "0h10m", "0h10m", "0h10m", "0h20m", "0h20m", "0h20m",
                "1h0m", "2h0m", "5h0m", "1h0m", "2h0m", "5h0m",
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
                "0h10m", "0h10m", "0h10m", "0h10m", "0h20m", "0h20m", "0h20m", "0h20m",
                "1h0m", "2h0m", "5h0m", "1h0m", "2h0m",
                "1h15m", "1h15m", "1h15m", "2h15m", "5h15m");
        List<String> actualList = engList.stream().map(s -> excelParser.prepareTimeFormat(s)).collect(Collectors.toList());
        engList.forEach(s -> excelParser.prepareTimeFormat(s));

        assertEquals(expectedList, actualList);
    }
    @Test
    public void testSumOfDurationPositive(){
        Duration one = new Duration(0,20);
        Duration two = new Duration(0,15);
        Duration three = new Duration(11,45);
        Duration four = new Duration(4,40);
        // a(named augend) + b(named addend) = c
        Duration augend = new Duration(0,0);
        Duration expected = new Duration(17,0);
        Duration actual = augend.add(one).add(two).add(three).add(four);
        assertEquals(expected, actual);
        assertEquals(new Duration(12, 5), one.add(three));
        assertEquals(new Duration(16, 25), three.add(four));
    }
    @Test
    public void testGetActivityFromRow(){
        Row row = sheet.getRow(0);
        Row row5 = sheet.getRow(5);
        Row row6 = sheet.getRow(6);
        Activity activity = excelParser.getActivity(row);
        assertNotNull(activity);

        Activity activity5 = excelParser.getActivity(row5);
        assertEquals("гитара", activity5.getName() );
        assertEquals(new Duration(0, 30), activity5.getDuration());

        Activity activity6 = excelParser.getActivity(row6);
        assertEquals("программирование", activity6.getName() );
        assertEquals(new Duration(4, 20), activity6.getDuration());
    }

}