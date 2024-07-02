import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testTaskPriority {
    // Checks task priority is correct.
    Priority expected;
    Priority negativeExpected;
    Priority actual;
    @BeforeEach
    void setUp() {
        expected=Priority.HIGH;
        negativeExpected= Priority.LOW;
    }

    @AfterEach
    void tearDown() {
        expected=null;
        negativeExpected=null;
    }

    @Test
    void testTaskPriorityPositive() {
        Task task=new Task();
        task.setPriority("1");
        actual=task.getPriority();
        assertEquals(expected,actual);
    }
    @Test
    void testTaskPriorityNegative() {
        Task task=new Task();
        task.setPriority("1");
        actual=task.getPriority();
        assertNotEquals(negativeExpected,actual);
    }
}