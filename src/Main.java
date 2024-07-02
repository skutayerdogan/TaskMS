import java.sql.SQLException;
import java.util.*;

public class Main {
    public static ArrayList<Task> taskList = new ArrayList<>();
    static HashMap<String, String> users = new HashMap<String, String>();
    static ArrayList<User> userList = new ArrayList<>();
    static boolean isLoggedIn=false;
    public static void main(String[] args) {
        // Database instance
        MyDatabase myDatabase = new MyDatabase();
        try{
            myDatabase.connect(); // try to connect local Mysql Database
            myDatabase.getUsers(userList,users); // get all Users in database and put in a ArrayList
            myDatabase.getTasks(taskList,userList); // get all Task in database and put in ArrayList and name password pair hashmap for authorization
            User currentUser = new User(); // temporary instance of  User class
            menu(currentUser,myDatabase); // Display static menu
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    static void menu(User currentUser, MyDatabase myDatabase) throws SQLException {
        Scanner input = new Scanner(System.in);
        while (!isLoggedIn) {
            System.out.println("1-Register"); // Menu Items
            System.out.println("2-Log in");
            System.out.println("3-Exit");
            String firstMenuSelection = input.nextLine();
            if (firstMenuSelection.equals("1")) {
                User.register(users, userList); // Registration
                myDatabase.createUser(userList.getLast()); //After Registration, adding new user in database
            } else if (firstMenuSelection.equals("2")) {
                currentUser = currentUser.logIn(users,userList); // Login
            } else if (firstMenuSelection.equals("3")) {
                System.out.println("Exiting..."); // Quiting System
                return;
            } else {
                System.out.println("Wrong Input!!!");
            }
        }
        while (true){
            System.out.println("Hello " + currentUser.getName());
            System.out.println("-------Menu-------");
            System.out.println("1-Show Tasks");
            System.out.println("2-Create New Task");
            System.out.println("3-Update Current Task");
            System.out.println("4-Search Task");
            System.out.println("5-Assign Task");
            System.out.println("6-Add Editor for Task");
            System.out.println("0-Log out");

            String menuSelection = input.nextLine();

            switch (menuSelection) {
                case "0":
                    System.out.println("Logging out..."); // log out
                    return;
                case "1":
                    showTask(currentUser); // show all Tasks function
                    break;
                case "2":
                    createTask(currentUser, myDatabase); // Creating a new Task function for current user
                    break;
                case "3":
                    System.out.println("Enter the id of the task you will update:");
                    int taskId = input.nextInt();
                    input.nextLine();
                    updateTask(currentUser, taskId, myDatabase); // Updating a Task.
                    break;
                case "4":
                    System.out.println("Search for tasks by id, name, owner or due date:\n1-Task ID\n2-Task Name\n3-Task Owner\n4-Task Due Date\n5-Logging out");
                    int choice = input.nextInt();
                    input.nextLine();
                    searchTask(currentUser, choice, myDatabase);
                    break;
                case "5":
                    System.out.println("Assign Task"); // Assigning task
                    assignTask(myDatabase,currentUser);
                    break;
                case "6":
                    System.out.println("Task Editor..."); // Assigning Editor
                    editorTask(myDatabase,currentUser);
                    break;
                default:
                    System.out.println("Wrong Input!!!");
                    break;
            }
        }
    }
    // Show all task in Task List
    static void showTask(User currentUser){
        // it sorts all task according to their priority.
        Collections.sort(taskList, new Comparator<Task>() { // sorts Tasks by their priority
            @Override
            public int compare(Task task1, Task task2) {
                return task1.getPriority().compareTo(task2.getPriority());
            }
        });
        for (Task task : taskList){
            System.out.println("*************************");
            System.out.println(task);
            if (task.isUserEditor(currentUser) || task.isUserAssignee(currentUser) || task.getOwner().equals(currentUser)){
                System.out.print(task.getEditors());
                System.out.print(task.getAssignees());
            }
            System.out.println("*************************");
        }
    }
    // Task Creating Process.
    static void createTask(User currentUser, MyDatabase myDatabase) throws SQLException {
        Scanner input = new Scanner(System.in);
        Task task = new Task();
        int idCounter = taskList.size();
        task.setTaskId(idCounter); //Declare idCounter for each creating Task because Task Id's should be unique.
        task.setOwner(currentUser);

        System.out.println("Enter Task name");
        String taskName = input.nextLine();
        task.setTaskName(taskName);

        System.out.println("Enter Task description");
        String taskDescription = input.nextLine();
        task.setDescription(taskDescription);

        System.out.println("Enter Task Priority\n1-HIGH\n2-MODERATE\n3-LOW");
        String priority = input.nextLine();
        task.setPriority(priority);

        System.out.println("Enter Task Start Date(yyyy-mm-dd)");
        String start = input.nextLine();
        task.setStart(start);

        System.out.println("Enter Task End Date(yyyy-mm-dd)");
        String end = input.nextLine();
        task.setEnd(end);

        taskList.add(task); // add new task to local list
        myDatabase.createTask(task); // database add task function
    }
    // Task Updating Process.
    static void updateTask(User currentUser, int taskId, MyDatabase myDatabase) throws SQLException {
        for (Task task : taskList){ // It iterates all Task list and finds by Task Id.
            if(task.getTaskId()==taskId){
                if (task.isUserAssignee(currentUser)){ // if user is assignee then he cannot update this task.
                    System.out.println("You are Assignee in this project. You have no permission to update this task.");
                    System.out.println("Users who have Permission to update:");
                    System.out.println(task.getOwner().getName() + " - Owner");
                    System.out.print(task.getEditors());
                    return;
                }
                if (!task.isUserEditor(currentUser) && !task.getOwner().equals(currentUser)){
                    System.out.println("You have no permission to update this task.");
                    return;
                }
                Scanner input = new Scanner(System.in);
                System.out.println("Enter new Task name");
                String taskName = input.nextLine();
                System.out.println(task.getTaskName()+" changed to "+taskName);
                task.setTaskName(taskName);

                System.out.println("Enter new Task description");
                String taskDescription = input.nextLine();
                System.out.println(task.getDescription()+" changed to "+taskDescription);
                task.setDescription(taskDescription);

                System.out.println("Enter new Task Priority\n1-HIGH\n2-MODERATE\n3-LOW");
                String priority = input.nextLine();
                String oldPriority=task.getPriority().toString();
                task.setPriority(priority);
                System.out.println(oldPriority+" changed to "+task.getPriority());

                System.out.println("Enter new Task Start Date(yyyy-mm-dd)");
                String start = input.nextLine();
                System.out.println(task.getStart()+" changed to "+start);
                task.setStart(start);

                System.out.println("Enter new Task End Date(yyyy-mm-dd)");
                String end = input.nextLine();
                System.out.println(task.getEnd()+" changed to "+end);
                task.setEnd(end);
                myDatabase.updateTask(task);
            }
        }
    }
    // It searches task with its whether name,id,owner name or due date.
    static void searchTask(User currentUser,int choice,MyDatabase myDatabase){
        Scanner input=new Scanner(System.in);
        String taskValue="";
        if (choice==1){
            System.out.println("Enter the task ID:");
            taskValue=input.nextLine();
            for(Task task:taskList){
                if (task.getTaskId()==Integer.parseInt(taskValue)){
                    System.out.println(task);
                }
            }
        }
        else if (choice==2){
            System.out.println("Enter the task name:");
            taskValue=input.nextLine();
            for(Task task:taskList){
                if(Objects.equals(task.getTaskName().toLowerCase(), taskValue.toLowerCase())){
                    System.out.println(task);
                }
            }
        }
        else if(choice==3){
            System.out.println("Enter the task owner:");
            taskValue=input.nextLine();
            for(Task task:taskList){
                if (task.getOwner().getName().toLowerCase().equals(taskValue.toLowerCase())){
                    System.out.println(task);
                }
            }

        }
        else if (choice==4){
            System.out.println("Enter the task due date:");
            taskValue=input.nextLine();
            for(Task task:taskList){
                if (task.getEnd().toString().equals(taskValue)){
                    System.out.println(task);
                }
            }
        }
        else{
            System.out.println("Invalid Input!");
        }
    }
    // Assigning Task process
    static void assignTask(MyDatabase database, User currentUser) throws SQLException {
        List <Task> userTaskList = new ArrayList<>(); // creating new user task list because it prevents if user selects task that he/she is not owned.
        // owners only assign peoples in tasks
        for(Task task : taskList){
            if (task.getOwner().getName().equals(currentUser.getName())){
                System.out.println(task);
                userTaskList.add(task); // add task in a new list if owner names are equal
            }
        }
        if (userTaskList.isEmpty()){
            System.out.println("You don't own any Task..."); // check the list is empty or not
            return;
        }
        System.out.println("Please enter the id of the task you want to assign team members");
        Scanner input = new Scanner(System.in);
        String taskId = input.nextLine();

        for (Task task : userTaskList){ // it loops all new task list to find id
            if (Integer.toString(task.getTaskId()).equals(taskId)){
                currentUser.assignTeamMembersToTask(database,userList,task);
                return;
            }
        }
        System.out.println("Invalid Id!!!");
    }
    // Assigning editor process
    static void editorTask(MyDatabase database, User currentUser) throws SQLException {
        List <Task> userEditorList = new ArrayList<>(); // creating new user task list because it prevents if user selects task that he/she is not owned.
        for(Task task : taskList){
            if (task.getOwner().getName().equals(currentUser.getName())){
                System.out.println("----------------------------");
                System.out.println(task);
                System.out.print(task.getEditors());
                System.out.print(task.getAssignees());
                userEditorList.add(task); // add task in a new list if owner names are equal
            }
        }
        if (userEditorList.isEmpty()){
            System.out.println("You don't own any Task..."); // check the list is empty or not
            return;
        }
        System.out.println("Please enter the id of the task you want to assign editor to your team members");
        Scanner input = new Scanner(System.in);
        String taskId = input.nextLine();

        for (Task task : userEditorList){
            if (Integer.toString(task.getTaskId()).equals(taskId)){
                currentUser.assignEditor(database,userList,task);
                return;
            }
        }
        System.out.println("Invalid Id");
    }
}