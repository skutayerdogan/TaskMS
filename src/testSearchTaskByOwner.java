import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class testSearchTaskByOwner {
    //In searchTask method, it performs search by given tasks owner name. if it finds return the intended task.
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
        expected="User";
        negativeExpected="user";
    }

    @AfterEach
    void tearDown() {
        expected="";
        negativeExpected="";

    }

    @Test
    void testSearchTaskByOwnerPositive() {
        String input="User";
        for (Task task:Main.taskList){
            if (task.getOwner().getName().equals(input)){
                actual=task.getOwner().getName();
            }
        }
        assertEquals(expected,actual);
    }
    @Test
    void testSearchTaskByOwnerNegative() {
        String input="User";
        for (Task task:Main.taskList){
            if (task.getOwner().getName().equals(input)){
                actual=task.getOwner().getName();
            }
        }
        assertNotEquals(negativeExpected,actual);
    }
}