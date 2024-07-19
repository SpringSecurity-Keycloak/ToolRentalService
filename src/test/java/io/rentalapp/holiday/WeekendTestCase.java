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
public class WeekendTestCase {

    @Test
    public void testNotWeekEnd() {
        Weekend weekend = new Weekend();
        LocalDate dateToTest = DataFormat.toLocalDate("07/19/2024");
        assertEquals(true,!weekend.isWeekend(dateToTest));
    }

    @Test
    public void testItFallsOnSunday() {
        Weekend weekend = new Weekend();
        LocalDate dateToTest = DataFormat.toLocalDate("07/21/2024");
        assertEquals(true,weekend.isWeekend(dateToTest));
    }

    @Test
    public void testItFallsOnSaturday() {
        Weekend weekend = new Weekend();
        LocalDate dateToTest = DataFormat.toLocalDate("07/20/2024");
        assertEquals(true,weekend.isWeekend(dateToTest));
    }

    @Test
    public void  testIsAnHoliday() {
        Weekend weekend = new Weekend();
        LocalDate dateToTest = DataFormat.toLocalDate("07/20/2024");
        assertEquals(true,weekend.isHoliday(dateToTest));
    }

}
