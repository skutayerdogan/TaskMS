import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDatabase {
    public Connection connection; // connection instance for database
    public void connect() throws SQLException {
        //Try to connect local mysql database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/taskify", "root", "12345678");
    }
    // It updates existing task. Find it by Task Id
    public void updateTask(Task updatedTask) throws SQLException {
        String sql = "UPDATE Tasks SET Name=?, Description=?, Priority=?, OwnerUserName=?, Start=?, End=? WHERE Id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, updatedTask.getTaskName());
        statement.setString(2,updatedTask.getDescription());
        statement.setString(3, String.valueOf(updatedTask.getPriority().ordinal()));
        statement.setString(4, String.valueOf(updatedTask.getOwner().getName()));
        statement.setString(5, String.valueOf(updatedTask.getStart()));
        statement.setString(6, String.valueOf(updatedTask.getEnd()));
        statement.setString(7, String.valueOf(updatedTask.getTaskId()));
        statement.executeUpdate();
    }
    // It gets all user records in database and put into users array for local data storage and hashmap for authorization
    public void getUsers(ArrayList<User> users, HashMap<String, String> userHashMap) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");
        while (resultSet.next()){
            User user = new User();
            user.setName(resultSet.getString("UserName"));
            user.setPassword(resultSet.getString("Password"));
            users.add(user); // add new user in array
            userHashMap.put(user.getName(),user.getPassword()); // add user's name and password pairs in hashmap
        }
    }
    // It gets all task records in database and put into local array
    public void getTasks(ArrayList<Task> tasks, ArrayList<User> userList) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Tasks");
        while (resultSet.next()){
            Task newTask = new Task();
            newTask.setTaskId(Integer.parseInt(resultSet.getString("Id"))); // Column names
            newTask.setTaskName(resultSet.getString("Name"));
            newTask.setDescription(resultSet.getString("Description"));
            newTask.setPriority(resultSet.getString("Priority"));
            newTask.setStart(resultSet.getString("Start"));
            newTask.setEnd(resultSet.getString("End"));
            String ownerUserName = resultSet.getString("OwnerUserName");
            for (User user: userList){
                // If ownerUserName matches with one of the user class. It sets Owner to that user class
                newTask.setOwner(new User());
                if (ownerUserName.contains(user.getName())){
                    newTask.setOwner(user);
                    break;
                }
            }
            newTask.setAssignedUserList(getAssignments(newTask,userList));
            newTask.setEditorsList(getEditors(newTask,userList));
            tasks.add(newTask);
        }
    }
    // get all assignees in database and add to the given task class.
    public ArrayList<User> getAssignments(Task task, ArrayList<User> users) throws SQLException {
        ArrayList <User> assignedUsers = new ArrayList<>();
        String sql = "SELECT UserName FROM Assignments WHERE TaskId=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(task.getTaskId()));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            for (User user : users){
                if (resultSet.getString(1).equals(user.getName()))
                    assignedUsers.add(user);
            }
        }
        return assignedUsers;
    }
    // get all assignees in database and add to the hashmap username,assigned task pairs.
    public HashMap <String,String> getAllAssignments() throws SQLException {
        HashMap <String,String> assignedUsers = new HashMap<>();
        String sql = "SELECT * FROM Assignments";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            assignedUsers.put(resultSet.getString(1),resultSet.getString(2));
        }
        return assignedUsers;
    }
    // sets assignment to given user with given id stores in database
    public void setAssignment(User user, int TaskId) throws SQLException {
        String sql = "INSERT INTO Assignments (UserName,TaskId) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, String.valueOf(TaskId));
        statement.executeUpdate();
    }
    // controls given user is a assignee in the given task if it is it returns true.
    public boolean IsTeamMemberInTask(int taskId, String userName) throws SQLException {
        String sql = "SELECT * FROM Assignments WHERE TaskId=? AND UserName=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(taskId));
        statement.setString(2, userName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }
    // delete assignment with given userName and task Id
    public void deleteAssignment(String userName, int taskId) throws SQLException {
        String sql = "DELETE FROM Assignments WHERE TaskId = ? AND UserName = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(taskId));
        statement.setString(2, userName);
        statement.executeUpdate();
    }
    // get all editors in database and add to the given task class.
    public ArrayList<User> getEditors(Task task, ArrayList<User> users) throws SQLException {
        ArrayList <User> editors = new ArrayList<>();
        String sql = "SELECT UserName FROM Editors WHERE TaskId=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(task.getTaskId()));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            for (User user : users){
                if (resultSet.getString(1).equals(user.getName()))
                    editors.add(user);
            }
        }
        return editors;
    }
    // add new editor into database
    public void setEditor(User user, int TaskId) throws SQLException {
        String sql = "INSERT INTO Editors (UserName,TaskId) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, String.valueOf(TaskId));
        statement.executeUpdate();
    }
    // check given user is editor in given task id, if it is returns true
    public boolean isEditor(int taskId, String userName) throws SQLException {
        String sql = "SELECT * FROM Editors WHERE TaskId=? AND UserName=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(taskId));
        statement.setString(2, userName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    // It adds newly created user into database
    public void createUser(User user) throws SQLException {

        String sql = "INSERT INTO Users (UserName,Password) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, user.getPassword());
        statement.executeUpdate();
    }
    // It deletes indicated user in database
    public void deleteUser(User user) throws SQLException {

        String sql = "DELETE FROM Users WHERE UserName=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.executeUpdate();
    }
    // It adds newly created task into database
    public void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO Tasks (Id, Name, Description, Priority, OwnerUserName, Start, End) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(task.getTaskId()));
        statement.setString(2, task.getTaskName());
        statement.setString(3,task.getDescription());
        statement.setString(4, String.valueOf(task.getPriority().ordinal()));
        statement.setString(5, String.valueOf(task.getOwner().getName()));
        statement.setString(6, String.valueOf(task.getStart()));
        statement.setString(7, String.valueOf(task.getEnd()));
        statement.executeUpdate();
    }
    // deletes given task in database
    public void deleteTask(Task task) throws SQLException {

        String sql = "DELETE FROM Tasks WHERE Id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(task.getTaskId()));
        statement.executeUpdate();
    }
}
