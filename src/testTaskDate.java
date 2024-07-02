import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class testTaskDate {
    // Checks start date is correct.
    String expected;
    String negativeExpected;
    String actual;
    @BeforeEach
    void setUp() {
        expected="2024-10-10";
        negativeExpected="2024-12-12";
    }

    @AfterEach
    void tearDown() {
        expected="";
        negativeExpected="";
    }

    @Test
    void testTaskDatePositive() {
        Task task=new Task();
        task.setStart("2024-10-10");
        actual=task.getStart().toString();
        assertEquals(expected,actual);
    }
    @Test
    void testTaskDateNegative() {
        Task task=new Task();
        task.setStart("2024-10-10");
        actual=task.getStart().toString();
        assertNotEquals(negativeExpected,actual);
    }
}