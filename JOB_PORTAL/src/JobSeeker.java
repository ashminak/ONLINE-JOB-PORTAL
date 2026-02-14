import java.util.Scanner;
import java.sql.*;
public class JobSeeker {
    static final String DB_url="jdbc:mysql://localhost:3306/job_portal";
    static final String username="root";
    static final String password="Ashmina@2000";
    static Scanner sc = new Scanner(System.in);
    static int loggedInUserId =-1;
    static void main(String[] args)throws ClassNotFoundException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        int choice = 0;
        while (choice!=4) {
            System.out.println("----JOBSEEKER MODULE----");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Jobs and Apply");
            System.out.println("4. Exit");
            System.out.print("Enter your Choice : ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    if (loggedInUserId != -1) {
                        viewJobsAndApply();
                        break;
                    } else {
                        System.out.println("Please Login First.");
                        ;
                    }
                case 4:
                    System.out.println("Exiting from JobSeeker Module......");
                    break;
                default:
                    System.out.println("Invalid choice!");


            }
        }
    }
    //INSERT USER ID AND JOB ID for register
    static void registerUser(){
        System.out.print("Enter Username : ");
        String name =sc.nextLine();
        System.out.print("Enter Password : ");
        String  pword =sc.nextLine();

        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username,password,role) " +
                    "VALUES (?,?,'jobSeeker')");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,pword);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Registration successful! Wait for admin approval.");
            }else System.out.println("Registration failed. Try again.");

        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }

    }
    //retrive if retrive succesfull for the given username and password means that user exit in database then print login successfull
    static void loginUser(){

        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM users " +
                    "WHERE username =? AND password =? AND role='jobSeeker' AND isActive = 1");
            System.out.print("Enter username: ");
            String name = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                loggedInUserId = resultSet.getInt("user_id");
                System.out.println("Login successful! Welcome " + name);
            }else {
                System.out.println("Invalid credentials or account not approved yet.");
            }


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    static void viewJobsAndApply(){
        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery("SELECT * FROM jobs WHERE isApproved = 1");
            System.out.println("--------View All jobs------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("job_id")+". "+resultSet.getString("title")+"- "+resultSet.getString("description"));
            }
            System.out.print("Enter Job ID to apply for job : ");
            int id = sc.nextInt();
            sc.nextLine();
            if(id==0)return;
            ///  TO CHECK IF HAVE APPLY EARLIER OR NOT
            PreparedStatement preparedStatement1 =connection.prepareStatement("SELECT * FROM applications" +
                    " WHERE user_id=? AND job_id =?");
            preparedStatement1.setInt(1,loggedInUserId);
            preparedStatement1.setInt(2,id);
            ResultSet resultSet1 =preparedStatement1.executeQuery();
            if (resultSet1.next()){
                System.out.println("You have already applied for this job.");
                return;
            }


            // TO APPLY FOR JOB
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO applications(user_id, job_id)VALUES (?,?)");
            preparedStatement.setInt(1,loggedInUserId);
            preparedStatement.setInt(2,id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("Job application submitted successfully!");
            }else {
                System.out.println("Application failed.");
            }
            showApplicationStatus();

        }catch (SQLException e){
            System.out.println("Error - "+e.getMessage());
        }

    }
    static void showApplicationStatus(){
        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.application_id,j.title,a.status " +
                    "FROM applications a JOIN jobs j ON a.job_id =j.job_id " +
                    "WHERE a.user_id=?");
            preparedStatement.setInt(1,loggedInUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println(resultSet.getInt("application_id")+"."+resultSet.getString("title")+" |Status: "+resultSet.getString("status"));
            }

        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }


}

