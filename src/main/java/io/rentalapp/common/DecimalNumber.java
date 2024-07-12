package io.rentalapp.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalNumber extends java.math.BigDecimal {

    public DecimalNumber(long value) {
        super(value);
    }

    /**
     *
     * @param value
     * @return
     */
    public static BigDecimal valueOf(double value) {
        BigDecimal roundUpValue = new BigDecimal(value);
        return roundUpValue.setScale(2, RoundingMode.HALF_UP);

    }
}
