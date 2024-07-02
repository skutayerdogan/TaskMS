import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testTaskId {
    // Checks id is correct.
    int expected;
    int negativeExpected;
    int actual;
    @BeforeEach
    void setUp() {
        expected=1;
        negativeExpected=2;
    }

    @AfterEach
    void tearDown() {
        expected=0;
        negativeExpected=0;
    }

    @Test
    void testTaskIdPositive() {
        Task task=new Task();
        task.setTaskId(1);
        actual= task.getTaskId();
        assertEquals(expected,actual);
    }
    @Test
    void testTaskIdNegative() {
        Task task=new Task();
        task.setTaskId(1);
        actual= task.getTaskId();
        assertNotEquals(negativeExpected,actual);
    }
}