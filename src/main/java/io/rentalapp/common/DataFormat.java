package io.rentalapp.common;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataFormat {

    private static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    /**
     * Convert the passed in String into a date. Throws a ValidationException if the string is not in
     * the expected format: "MM-dd-yyyy
     * @param date
     * @return
     */
    public static Date parseDate(String date)  {

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
    public static String toDateString(Date date) {
        return formatter.format(date);
    }

    /**
     *
     * @param value
     * @return
     */
    public static final String toPercentString(BigDecimal value) {
       return  String.format( "%s%%", value.toPlainString());
    }
}
