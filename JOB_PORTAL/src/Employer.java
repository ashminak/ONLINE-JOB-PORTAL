import java.util.Scanner;
import java.sql.*;

public class Employer {
    static final String DB_url = "jdbc:mysql://localhost:3306/job_portal";
    static final String username = "root";
    static final String password = "Ashmina@0000";
    static Scanner sc = new Scanner(System.in);
    static int loggedInEmployerId = -1;

    static void main(String[] args) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded Successfully.");

        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load Driver : " + e.getMessage());
        }
        int choice = 0;
        while (choice != 4) {
            System.out.println("------Employer Module--------");
            System.out.println("1.Register ");
            System.out.println("2.Login");
            System.out.println("3.Manage Jobs");
            System.out.println("4.Exits");
            System.out.print("Enter your choice - ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    registerEmployer();
                    break;
                case 2:
                    loginEmployer();
                    break;
                case 3:
                    if (loggedInEmployerId != -1) {
                        manageJobs();
                    } else {
                        System.out.println("Please Login First");
                    }
                    break;
                case 4:
                    System.out.println("Exiting Employer Module........");
                    break;
                default:
                    System.out.println("Invalid choice!");

            }
        }
    }
    //INSERT USER ID AND JOB ID for register

    static void registerEmployer() {
        try {
            Connection connection = DriverManager.getConnection(DB_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username,password, role)" +
                    "VALUES (?,?,'employer')");
            System.out.print("Enter your Username: ");
            String name = sc.nextLine();
            System.out.print("Enter your Password: ");
            String pword = sc.nextLine();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pword);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Registration successful! Wait for admin approval.");
            } else {
                System.out.println("Registration failed. Try again.");
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
    //retrive if retrive succesfull for the given username and password means that user exit in database then print login successfull

    static void loginEmployer() {
        try {
            Connection connection = DriverManager.getConnection(DB_url, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password =? AND role= 'employer' AND IsActive =1 ");
            System.out.print("Enter your Username: ");
            String name = sc.nextLine();
            System.out.print("Enter your Password: ");
            String pword = sc.nextLine();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pword);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loggedInEmployerId = resultSet.getInt("user_id");
                System.out.println("Login successful! Welcome " + name);
            } else {
                System.out.println("Invalid credentials or account not approved yet.");

            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    static void manageJobs() {
        int choice = 0;
        while (choice != 5) {
            System.out.println("---------JOB MANAGEMENT------");
            System.out.println("1.Post New Job");
            System.out.println("2.View Your Job");
            System.out.println("3.View Applications for a Job");
            System.out.println("4.Update Application Status");
            System.out.println("5.Back");
            System.out.print("Enter your Choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    postJob();
                    break;
                case 2:
                    viewYourJobs();
                    break;
                case 3:
                    viewApplications();
                    break;
                case 4:
                    updateApplicationStatus();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Choice!");

            }
        }
    }
    static void postJob(){
        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement =connection.prepareStatement("INSERT INTO jobs(title,description) VALUES (?,?)");
            System.out.println("Enter Job title : ");
            String title = sc.nextLine();
            System.out.println("Enter Job description : ");
            String description = sc.nextLine();
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,description);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("Job posted successfully! Awaiting admin approval.");
            }else {
                System.out.println("Failed to post job.");
            }

        }catch (SQLException e){
            System.out.println("Error : "+e.getMessage());
        }

    }
    static void viewYourJobs(){
        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            Statement statement = connection.createStatement();
            ResultSet resultSet =statement.executeQuery("SELECT  * FROM jobs");
            System.out.println("------ALL JOBS-------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("job_id")+"."+resultSet.getString("title")+" |Status "+resultSet.getInt("isApproved"));
            }
        }catch (SQLException e){
            System.out.println("Error "+e.getMessage());
        }
    }
    static void viewApplications(){
        try {
            Connection connection =DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.application_id, u.username, a.status " +
                    "FROM applications a " +
                    "JOIN users u ON a.user_id=u.user_id " +
                    "WHERE a.job_id =?");
            System.out.println("Enter job id to check applications : ");
            int id = sc.nextInt();
            preparedStatement.setInt(1,id);
            sc.nextLine();
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                System.out.println("Application ID : "+resultSet.getInt("application_id")+"."+" Username : "+resultSet.getString("username")+" | "+resultSet.getString("status"));
            }

        }catch (SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }
    static void updateApplicationStatus(){
        try {
            Connection connection = DriverManager.getConnection(DB_url,username,password);
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE applications " +
                    "SET status =? " +
                    "WHERE application_id=?");
            System.out.println("Enter new status (Applied/Shortlisted/Rejected/Selected):");
            String status = sc.nextLine();
            System.out.println("Enter Application ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected>0){
                System.out.println("Application status updated successfully!");
            }else {
                System.out.println("Application not found.");
            }
        }catch (SQLException e){
            System.out.println("Error : "+e.getMessage());
        }
    }

}

