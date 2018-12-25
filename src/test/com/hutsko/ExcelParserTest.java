package com.hutsko;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        List<String> rows = excelParser.parse(sheet);
        assertEquals(8, rows.size());
    }

    @Test
    public void testReadUniqueDaysLines(){}
}