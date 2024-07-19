package io.rentalapp.holiday;

import io.rentalapp.common.DataFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class LaborDayTestCase {

    @Test
    public void testItFallsOnWeekday() {
        LaborDay laborDay = new LaborDay();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(true,laborDay.isHoliday(dateToTest));
    }

    @Test
    public void testNotFallsOnSunday() {
        LaborDay laborDay = new LaborDay();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(false,laborDay.isWeekend(dateToTest));
    }

    @Test
    public void testNotFallsOnSaturday() {
        LaborDay laborDay = new LaborDay();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(false,laborDay.isWeekend(dateToTest));
    }

    @Test
    public void  testIsNotAnHoliday() {
        LaborDay laborDay = new LaborDay();
        LocalDate dateToTest = DataFormat.toLocalDate("09/03/2024");
        assertEquals(false,laborDay.isHoliday(dateToTest));
    }

}
