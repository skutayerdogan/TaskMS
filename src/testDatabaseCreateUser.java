import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class testDatabaseCreateUser {
    // It test createUser function. Checks does database stores User proper way.
    //  To check we call function getUsers to return all User store in database.
    MyDatabase database;
    User testUser;
    String expected;
    String negativeExpected;
    String actual;
    @BeforeEach
    void setUp() throws SQLException {
        database = new MyDatabase();
        database.connect();
        testUser = new User("test","1234"); // creating Test User
        expected = "1234";
        negativeExpected = "asdf";
    }

    @AfterEach
    void tearDown() throws SQLException {
        database.deleteUser(testUser); // after finished the test we delete testUser in database
        Main.userList.clear(); // clear temporary storages
        Main.users.clear();
    }

    @Test
    void createUserPositive() throws SQLException {
        database.createUser(testUser); // store user in database
        database.getUsers(Main.userList,Main.users); // get all user in database and add to local data storages
        actual = Main.users.get("test"); // if there is a User that name is test it should return his password
        assertEquals(expected,actual);
    }
    @Test
    void createUserNegative() throws SQLException {
        database.createUser(testUser);
        database.getUsers(Main.userList,Main.users);
        actual = Main.users.get("test");
        assertNotEquals(negativeExpected,actual); // it should not return another password
    }
}