package io.rentalapp.common;

import io.rentalapp.api.model.RentalAgreement;
import io.rentalapp.api.model.Tool;
import io.rentalapp.persist.entity.RentalAgreementEntity;
import io.rentalapp.persist.entity.ToolEntity;

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
}
