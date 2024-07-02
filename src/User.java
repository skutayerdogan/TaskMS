import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    // User object fields. getter and setters
    private String username;
    private String password;
    // constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    // default constructor
    public User() {}

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Pattern pattern = Pattern.compile("[1-9]+");
        Matcher matcher = pattern.matcher(password);
        if (matcher.find())
            this.password = password;
        else {
            System.out.println("Password is incorrect!! It should be number");
            System.out.println("Setting Password as 1234");
            this.password = "1234";
        }
    }

    @Override
    public String toString() {
        return username;
    }
    // Registration with User Name and password.
    public static void register(HashMap<String, String> userHashMap, ArrayList<User> userList){
        Scanner input = new Scanner(System.in);
        boolean isRegistered = false;

        while (!isRegistered) {
            System.out.println("Please enter a username:");
            String username = input.nextLine();

            if (userHashMap.containsKey(username)) {
                System.out.println("This username already exists.");
            } else {
                System.out.println("Please enter a password:");
                String password = input.nextLine();
                User newUser = new User();
                newUser.setName(username);
                newUser.setPassword(password);
                userList.add(newUser);
                userHashMap.put(newUser.getName(), newUser.getPassword());
                isRegistered = true;
                System.out.println("Registered successfully");
            }
        }
    }
    // Log-In with UserName and password.
    public User logIn(HashMap<String, String> users,ArrayList<User> userList) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter an username");
        username = input.nextLine();
        System.out.println("Please enter a password");
        password = input.nextLine();
        if (!users.containsKey(username)) {
            System.out.println("Invalid username or password");
        } else {
            if (users.get(username).equals(password)) {
                System.out.println("Logged in successfully");
                Main.isLoggedIn = true;
                for (User user : userList)
                    if (user.getName().equals(username))
                        return user;
            } else {
                System.out.println("Invalid username or password");
            }
        }
        return this;

    }
    // assign task method to given task
    public void assignTeamMembersToTask(MyDatabase database,ArrayList<User> userList, Task task) throws SQLException {
        // show team members that already assigned this project
        System.out.println("Assigned Team members in " + task.getTaskName() + " Task");
        for (User user : task.getAssignedUserList()){
            System.out.println(user);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Please select Users to Assign " + task.getTaskName() + " Task");
        // show Users that not in this project
        for (User user : userList){
            if (task.getAssignedUserList().contains(user) && task.getOwner().getName().equals(this.getName()))
                continue;
            System.out.println(user);
        }
        Scanner input = new Scanner(System.in);
        String userName = input.nextLine();
        // cannot assign yourself it returns main menu
        if (userName.equals(this.getName())){
            System.out.println("You cannot add yourself in the Assign list!!!");
            return;
        }
        // Selected person is already in assign list then returns main menu
        else if (database.IsTeamMemberInTask(task.getTaskId(),userName)){
            System.out.println(userName + " is already in the current task!!!");
            return;
        }
        // iterates all users and finds the selected user. Add to local storage.
        // And pass variables to database class to add assignment record to database
        // if cannot find it print warning
        for(User user : userList){
            if (userName.equals(user.getName())){
                database.setAssignment(user,task.getTaskId());
                task.getAssignedUserList().add(user);
                System.out.println(user.getName() + " added successfully in " + task.getTaskName() + " task as assignee");
                return;
            }
        }
        System.out.println("Cannot found any records!");
    }
    public void assignEditor(MyDatabase database,ArrayList<User> userList, Task task) throws SQLException {
        System.out.println("Editors in " + task.getTaskName() + " Task");
        for (User user : task.getEditorsList()){
            System.out.println(user);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Assigned Team members in " + task.getTaskName() + " Task");
        for (User user : task.getAssignedUserList()){
            System.out.println(user);
        }
        System.out.println("-----------------------------------------------");
        System.out.println("Please select Users to Assign " + task.getTaskName() + " Task as a Editor in Assigned Users.");
        Scanner input = new Scanner(System.in);
        String userName = input.nextLine();
        if (userName.equals(this.getName())){
            System.out.println("You cannot add yourself in the Editor list!!!");
            return;
        } else if (database.isEditor(task.getTaskId(),userName)) {
            System.out.println(userName + " is Already Editor");
            return;
        }
        else if (!database.IsTeamMemberInTask(task.getTaskId(),userName)){
            System.out.println(userName + " is not in Task!!!");
            return;
        }
        for(User user : userList){
            if (userName.equals(user.getName())){
                database.setEditor(user,task.getTaskId());
                task.getEditorsList().add(user);
                System.out.println(user.getName() + " added successfully in " + task.getTaskName() + " task as editor");
                database.deleteAssignment(user.getName(),task.getTaskId());
                task.getAssignedUserList().remove(user);
                return;
            }
        }
        System.out.println("Cannot found any records!");
    }

}
