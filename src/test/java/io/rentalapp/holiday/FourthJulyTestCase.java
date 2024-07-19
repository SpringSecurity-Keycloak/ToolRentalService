package io.rentalapp.holiday;

import io.rentalapp.common.DataFormat;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(
        locations = "classpath:application.properties")
public class FourthJulyTestCase {

    @Test
    public void testItFallsOnWeekday() {
        FourthJuly fourthJuly = new FourthJuly();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2024");
        assertEquals(true,fourthJuly.isHoliday(dateToTest));
    }

    @Test
    public void testItFallsOnSunday() {
        FourthJuly fourthJuly = new FourthJuly();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2021");
        assertEquals(true,fourthJuly.isWeekend(dateToTest));
    }

    @Test
    public void testItFallsOnSaturday() {
        FourthJuly fourthJuly = new FourthJuly();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2020");
        assertEquals(true,fourthJuly.isWeekend(dateToTest));
    }

    @Test
    public void  testIsNotAnHoliday() {
        FourthJuly fourthJuly = new FourthJuly();
        LocalDate dateToTest = DataFormat.toLocalDate("07/05/2024");
        assertEquals(false,fourthJuly.isHoliday(dateToTest));
    }

}
