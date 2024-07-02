import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class testSearchTaskByName {
    //In searchTask method, it performs search by given task name. if it finds return the intended task.
    String expected;
    String negativeExpected;
    String actual;
    User user=new User("User","1234");
    Task task1=new Task("Task1",1,"Task1",user, LocalDate.parse("2024-10-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse("2024-10-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    Task task2=new Task("Task2",2,"Task2",user, LocalDate.parse("2024-12-10", DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalDate.parse("2024-12-15", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    @BeforeEach
    void setUp() {
        Main.taskList.add(task1);
        Main.taskList.add(task2);
        expected="Task1";
        negativeExpected="Task3";
    }

    @AfterEach
    void tearDown() {
        expected="";
        negativeExpected="";

    }

    @Test
    void testSearchTaskByNamePositive() {
        String input="Task1";
        for (Task task:Main.taskList){
            if (task.getTaskName().equals(input)){
                actual=task.getTaskName();
            }
        }
        assertEquals(expected,actual);
    }
    @Test
    void testSearchTaskByNameNegative() {
        String input="Task1";
        for (Task task:Main.taskList){
            if (task.getTaskName().equals(input)){
                actual=task.getTaskName();
            }
        }
        assertNotEquals(negativeExpected,actual);
    }
}