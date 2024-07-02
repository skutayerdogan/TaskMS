import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class testDatabaseCreateTask {
    // It test createUser function. Checks, does database stores User proper way.
    //  To check we call function getUsers to return all User store in database.
    MyDatabase database;
    Task testTask;
    int expected;
    int negativeExpected;
    int actual;

    @BeforeEach
    void setUp() throws SQLException {
        database = new MyDatabase();
        database.connect();
        // creating Test Task
        testTask = new Task("test",1000,"test",new User("testuser","1234"), LocalDate.now().plusDays(1),LocalDate.now().plusDays(2));
        expected = 1000; // id of the new task
        negativeExpected = 2000;
    }

    @AfterEach
    void tearDown() throws SQLException {
        Main.taskList.clear();
        Main.userList.clear();
        database.deleteTask(testTask);
        database = null;
        testTask = null;
    }

    @Test
    void createTaskPositive() throws SQLException {
        database.createTask(testTask); // add testTask to database
        database.getTasks(Main.taskList,Main.userList); // get all task in database
        for (Task task : Main.taskList)
            if (task.getTaskId() == 1000) // look for id because id is unique.
                actual = task.getTaskId(); // if there is a task id is 1000 it assign that id to actual if its not not assign anything
        assertEquals(expected,actual);
    }
    @Test
    void createTaskNegative() throws SQLException {
        database.createTask(testTask);
        database.getTasks(Main.taskList,Main.userList);
        actual = 2000; // set actual id to 2000 if there is no task which id is 1000 actual value remains 2000
        for (Task task : Main.taskList)
            if (task.getTaskId() == 1000)
                actual = task.getTaskId();
        assertNotEquals(negativeExpected,actual);
    }

}