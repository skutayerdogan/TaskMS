import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class testSearchTaskById {
    //In searchTask method, it performs search by given task id. if it finds return the intended task.
    int expected;
    int negativeExpected;
    int actual;
    User user=new User("User","1234");
    Task task1=new Task("Task1",1,"Task1",user, LocalDate.parse("2024-10-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse("2024-10-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    Task task2=new Task("Task2",2,"Task2",user, LocalDate.parse("2024-12-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse("2024-12-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    @BeforeEach
    void setUp() {
        Main.taskList.add(task1);
        Main.taskList.add(task2);
        expected=1;
        negativeExpected=3;
    }

    @AfterEach
    void tearDown() {
        expected=0;
        negativeExpected=0;

    }

    @Test
    void testSearchTaskByIdPositive() {
        int input=1;
        for (Task task:Main.taskList){
            if (task.getTaskId()==input){
                actual=task.getTaskId();
            }
        }
        assertEquals(expected,actual);
    }
    @Test
    void testSearchTaskByIdNegative() {
        int input=1;
        for (Task task:Main.taskList){
            if (task.getTaskId()==input){
                actual=task.getTaskId();
            }
        }
        assertNotEquals(negativeExpected,actual);
    }
}