package com.hutsko;

import com.hutsko.entity.Activity;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.hutsko.ExcelParser.PATH_FILE;
import static com.hutsko.ExcelParser.SHEET_NAME;

public class Main {
//    private static final String PATH_TEST_SHEET = "src/test/resources/testsheet.xlsx";
//    private static final String SHEET_NAME = "Sheet1";

    private static final String NAME_PROG = "программирование";
    private static final String NAME_GUITAR = "гитара";
    private static final String NAME_READING = "чтение";
    private static final String NAME_MATH = "математика";
    private static final String NAME_ENG = "английский";

    public static void main(String[] args) {
        ExcelParser ep = new ExcelParser(PATH_FILE);
        Sheet sheet = ep.readSheet(SHEET_NAME);
        Map<LocalDate, List<Activity>> storage = ep.getAllDays(sheet);

        int dayCounter = storage.size();
        Activity programming = new Activity(NAME_PROG);
        Activity eng = new Activity(NAME_ENG);
        Activity guitar = new Activity(NAME_GUITAR);
        Activity reading = new Activity(NAME_READING);
        Activity math = new Activity(NAME_MATH);

        for (Map.Entry<LocalDate, List<Activity>> entry : storage.entrySet()) {
            List<Activity> activities = entry.getValue();

            for (Activity activity : activities) {
                switch (activity.getName()) {
                    case NAME_PROG:
                        programming.add(activity.getDuration());
                        break;
                    case NAME_ENG:
                        eng.add(activity.getDuration());
                        break;
                    case NAME_GUITAR:
                        guitar.add(activity.getDuration());
                        break;
                    case NAME_READING:
                        reading.add(activity.getDuration());
                        break;
                    case NAME_MATH:
                        math.add(activity.getDuration());
                        break;

                }
            }
        }
        System.out.println("----------------------");
        System.out.println("Days spend for learning: " + dayCounter);
        System.out.println("Time for Programming: " + programming);
        System.out.println("Time for English: " + eng);
        System.out.println("Time for Guitar: " + guitar);
        System.out.println("Time for Reading: " + reading);
        System.out.println("Time for Math: " + math);
        System.out.println("----------------------");

    }
}
