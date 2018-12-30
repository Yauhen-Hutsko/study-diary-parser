package com.hutsko;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        List<Row> rows = excelParser.parse(sheet);
        assertEquals(8, rows.size());
    }

    @Test
    public void testGetAllDays() {
        List<Row> rows = excelParser.parse(sheet);
        Map<String, List<Row>> realDays = excelParser.getAllDays(rows);
        assertEquals(7, realDays.size());
    }

    @Test
    public void testParseDate() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d,mm,yy");
        String timeOne = "1,12,18";

        assertEquals(LocalDate.of(2018, 12, 1), excelParser.parseDate(timeOne));
        System.out.println(timeOne);
    }
}