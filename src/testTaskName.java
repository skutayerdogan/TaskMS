import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testTaskName {
    // Checks task name is correct.
    String expected;
    String negativeExpected;
    String actual;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        expected="Task";
        negativeExpected="task";
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        expected="";
        negativeExpected="";
    }

    @Test
    void testTaskNamePositive() {
        Task task=new Task();
        task.setTaskName("Task");
        actual= task.getTaskName();
        assertEquals(expected,actual);
    }
    @Test
    void testTaskNameNegative() {
        Task task=new Task();
        task.setTaskName("Task");
        actual= task.getTaskName();
        assertNotEquals(negativeExpected,actual);
    }
}