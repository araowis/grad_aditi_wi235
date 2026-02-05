package utils;

import java.sql.Date;

public class DateInput {
    public static Date parseDate(String dateStr) throws IllegalArgumentException {
        String[] parts = dateStr.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected DD-MM-YYYY");
        }

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        return new Date(year - 1900, month - 1, day);
    }
}
