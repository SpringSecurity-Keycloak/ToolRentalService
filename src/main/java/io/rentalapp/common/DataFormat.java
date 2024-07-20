package io.rentalapp.common;

import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.Tool;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import io.rentalapp.persist.entity.ToolEntity;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DataFormat {

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Convert the passed in String into a date. Throws a ValidationException if the string is not in
     * the expected format: "MM-dd-yyyy
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr)  {

        LocalDate date = toLocalDate(dateStr);
        return toDate(date);
    }

    /**
     * Parse the date into a formatted string with pattern MM/dd/yyyy
     * @param date
     * @return
     */
    public static String toDateString(Date date) {
        LocalDate localDate = toLocalDate(date);
        return localDate.format(format);
    }

    /**
     * Convert java.time.LocalDate to java.util.date
     * @param date
     * @return
     */
    public static Date toDate(LocalDate date) {
      return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) ;
    }
    /**
     * Return a string value of the format ###%
     * @param value
     * @return
     */
    public static final String toPercentString(BigDecimal value) {
       return  String.format( "%s%%", value.toPlainString());
    }

    /**
     *
     * @param rentalAgreement
     * @return
     */
    public static RentalAgreement toRentalAgreement(RentalAgreementEntity rentalAgreement) {
        RentalAgreement agreement = new RentalAgreement();
        agreement.setDiscountAmount(rentalAgreement.getDiscountAmount());
        agreement.setDiscountPercent(rentalAgreement.getDiscountPercent().toPlainString());
        agreement.setPreDiscountCharge(rentalAgreement.getPreDiscountCharge());
        agreement.setDailyCharge(rentalAgreement.getDailyCharge());
        agreement.setRentalDays(String.valueOf(rentalAgreement.getRentalDays()));
        agreement.setFinalCharge(rentalAgreement.getFinalCharge());
        agreement.setToolType(rentalAgreement.getToolType());
        agreement.setChargeDays(BigDecimal.valueOf(rentalAgreement.getChargeDays()));
        agreement.setDueDate(DataFormat.toDateString(rentalAgreement.getDueDate()));
        agreement.setToolBrand(rentalAgreement.getToolBrand());
        agreement.setToolType(rentalAgreement.getToolType());
        agreement.setToolCode(rentalAgreement.getToolCode());
        agreement.setCheckoutDate(DataFormat.toDateString(rentalAgreement.getCheckoutDate()));
        return agreement;
    }

    /**
     *
     * @param toolEntity
     * @return
     */
    public static Tool toTool(ToolEntity toolEntity) {
        Tool tool = new Tool();
        tool.setBrand(toolEntity.getBrand());
        tool.setCode(toolEntity.getCode());
        tool.type(toolEntity.getType());
        return tool;
    }

    /**
     *
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(String date) {
        try {
            return LocalDate.parse(date, format);
        }catch (Exception ex) {
            throw new ValidationException( "Dates should be in format MM/dd/yyyy. for e.g 12/31/2024" );
        }

    }

    /**
     *
     * @param date
     * @return
     */
    public static LocalDate toLocalDate(Date date) {
        return  date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
