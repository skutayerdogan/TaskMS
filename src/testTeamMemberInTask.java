import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class testTeamMemberInTask {
    // test the team member in the task or not.
    // if it is, it should return true if not, it should return false
    MyDatabase database;
    boolean expected;
    boolean negativeExpected;
    boolean actual;
    User userInput;
    int idInput;
    @BeforeEach
    void setUp() throws SQLException {
        database = new MyDatabase();
        database.connect();
        expected = true;
        negativeExpected = false;
        userInput = new User("test","1234"); // creating test User
        idInput = 100; // test Id
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.deleteAssignment(userInput.getName(), idInput); // after finishes it deletes test assignment
        userInput = null;
        idInput = 0;
        expected = false;
        negativeExpected = false;
        actual = false;
    }

    @Test
    void isTeamMemberInTaskPositive() throws SQLException {
        database.setAssignment(userInput, idInput); // adding test input in database
        actual = database.IsTeamMemberInTask(100,"test"); // if given inputs is in database it returns true.
        assertEquals(expected,actual);
    }
    @Test
    void isTeamMemberInTaskNegative() throws SQLException {
        database.setAssignment(userInput, idInput); // adding test input in database
        actual = database.IsTeamMemberInTask(100,"test"); // if given inputs is not in database it returns false.
        assertNotEquals(negativeExpected,actual);
    }
}