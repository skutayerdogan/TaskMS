import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class testTaskOwner {
    // Checks task owner name is correct.
    String expected;
    String negativeExpected;
    String actual;
    @BeforeEach
    void setUp() {
        expected="User";
        negativeExpected="user";
    }

    @AfterEach
    void tearDown() {
        expected="";
        negativeExpected="";
    }

    @Test
    void testTaskOwnerPositive() {
        User user=new User("User","1234");
        Task task=new Task();
        task.setOwner(user);
        actual=task.getOwner().getName();
        assertEquals(expected,actual);
    }
    @Test
    void testTaskOwnerNegative() {
        User user=new User("User","1234");
        Task task=new Task();
        task.setOwner(user);
        actual=task.getOwner().getName();
        assertNotEquals(negativeExpected,actual);
    }
}