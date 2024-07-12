package io.rentalapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class to handle common date related functionality
 */
public class DateUtility {

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    /**
     * Convert the passed in String into a date. Throws a ValidationException if the string is not in
     * the expected format: "MM-dd-yyyy
     * @param date
     * @return
     */
    public Date parseDate(String date)  {

        Date result = null;
        try {
            formatter.setLenient(false);
            result = formatter.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Expected date format is MM/dd/YYYY. for e.g 12/31/2024");
        }
        return result;
    }

    /**
     * Parse the date into a formatted string with pattern MM/dd/yyyy
     * @param date
     * @return
     */
    public String format(Date date) {
        return formatter.format(date);
    }
}
