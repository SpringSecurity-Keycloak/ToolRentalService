package io.rentalapp.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

    /**
     *
     * @param date
     * @return
     */
    public Date parseDate(String date)  {

        Date result = null;
        try {
            formatter.setLenient(false);
            result = formatter.parse(date);
        } catch (ParseException e) {
            throw new ValidationException("Expected date format is MM-dd-YYYY. for e.g 12-31-2024");
        }
        return result;
    }

    /**
     *
     * @param date
     * @return
     */
    public String format(Date date) {
        return formatter.format(date);
    }
}
