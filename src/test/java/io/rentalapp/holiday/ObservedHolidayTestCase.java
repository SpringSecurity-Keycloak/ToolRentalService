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
public class ObservedHolidayTestCase {

    @Test
    public void testItFallsOnWeekday() {
        ObservedHoliday fourthJuly = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2024");
        assertEquals(true,fourthJuly.isHoliday(dateToTest));
    }

    @Test
    public void testItFallsOnSunday() {
        ObservedHoliday fourthJuly = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2021");
        assertEquals(true,fourthJuly.isWeekend(dateToTest));
    }

    @Test
    public void testItFallsOnSaturday() {
        ObservedHoliday fourthJuly = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("07/04/2020");
        assertEquals(true,fourthJuly.isWeekend(dateToTest));
    }

    @Test
    public void  testIsNotAnHoliday() {
        ObservedHoliday fourthJuly = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("07/05/2024");
        assertEquals(false,fourthJuly.isHoliday(dateToTest));
    }

    @Test
    public void testItFallsOnWeekday_1() {
        ObservedHoliday laborDay = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(true,laborDay.isHoliday(dateToTest));
    }

    @Test
    public void testNotFallsOnSunday() {
        ObservedHoliday laborDay = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(false,laborDay.isWeekend(dateToTest));
    }

    @Test
    public void testNotFallsOnSaturday() {
        ObservedHoliday laborDay = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("09/02/2024");
        assertEquals(false,laborDay.isWeekend(dateToTest));
    }

    @Test
    public void  testIsNotAnHoliday_1() {
        ObservedHoliday laborDay = new ObservedHoliday();
        LocalDate dateToTest = DataFormat.toLocalDate("09/03/2024");
        assertEquals(false,laborDay.isHoliday(dateToTest));
    }
}
