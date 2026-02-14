import java.sql.*;
import java.util.Scanner;


public class Admin {
    static final String DB_Url = "jdbc:mysql://localhost:3306/job_portal";
    static final String userName = "root";
    static final String passWord = "Ashmina@0000";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Load Error :" + e.getMessage());
        }
        if (!adminLogin()) {
            return;
        }
        int choice = 0;
        while(choice != 5) {
            System.out.println("..........ADMIN MODULE..........");
            System.out.println("1.Approve Users");
            System.out.println("2.Approve Job postings");
            System.out.println("3.View Users");
            System.out.println("4.View Job Postings");
            System.out.println("5.Exits");
            System.out.print("Enter your choice : ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    approveUsers();
                    break;
                case 2:
                    approveJobpostings();
                    break;
                case 3:
                    viewUsers();
                    break;
                case 4:
                    viewJobpostings();
                    break;
                case 5:
                    // will create the exits module
                    System.out.println("Exiting Admin Module.............");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        }

    }

    static boolean adminLogin() {
        System.out.println("---------------Admin Login---------------");
        System.out.print("Enter username:");
        String uName = sc.nextLine();
        System.out.print("Enter password:");
        String pword = sc.nextLine();
        try {
            Connection connection = DriverManager.getConnection(DB_Url, userName, passWord);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(username, password, role, isActive)"+
                   " VALUES (?, ?, 'admin', 1)");
            preparedStatement.setString(1, uName);
            preparedStatement.setString(2, pword);
            int rowsAffected= preparedStatement.executeUpdate();
            if (rowsAffected>0) {
                System.out.println("Login Successful");
                return true;
            } else {
                System.out.println("Invalid Credentials! Or Not an Admin");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Database Error :" + e.getMessage());
            return false;
        }

    }
    // VIEW PENDING USERS AND APPROVE THEM
    static void  approveUsers(){
        try{
            Connection connection = DriverManager.getConnection(DB_Url,userName,passWord);
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE  role = !'admin' AND isActive =0 ");
            System.out.println("--------------PENDING USERS---------------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("user_id")+". "+resultSet.getString("username")+"("+resultSet.getString("role")+")");
            }
            System.out.println("---------Approve Pending Users-----------");
            System.out.print("Enter User ID to approve: ");
            int u_id = sc.nextInt();
            sc.nextLine();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET isActive = 1 " +
                    "WHERE user_id = ?");
            preparedStatement.setInt(1,u_id);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected >0){
                System.out.println("User Approved Successfully");
            }else {
                System.out.println("User not found or already Approved.");
            }

        }catch (SQLException e){
            System.out.println("Error "+e.getMessage());
        }

    }
    // VIEW PENDING JOB POSTINGS AND APPROVE THEM
    static void approveJobpostings(){
        try{
            Connection connection = DriverManager.getConnection(DB_Url,userName,passWord);
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM jobs WHERE isApproved =0");
            System.out.println("-------Pending Jobs--------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("job_id")+". "+resultSet.getString("title")+"-"+resultSet.getString("description"));
            }
            System.out.println("------Approved Pending Jobs--------");
            System.out.print("Enter Job ID to Approve-");
            int id = sc.nextInt();
            sc.nextLine();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE jobs SET isApproved =1 " +
                    "WHERE job_id = ?");
            preparedStatement.setInt(1,id);
            int rowsaffected = preparedStatement.executeUpdate();
            if (rowsaffected>0){
                System.out.println("Job Approved Successfully.");
            }else System.out.println("Job not found or already Approved.");

        }catch (SQLException e){
            System.out.println("Error "+e.getMessage());
        }
    }
    static void viewUsers(){
        try{
            Connection connection = DriverManager.getConnection(DB_Url,userName,passWord);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE  role !='admin'");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("user_id")+"." +resultSet.getString("username ")+"Role - "+resultSet.getString("role")+"|"+" Status - "+resultSet.getInt("isActive"));
            }

        }catch (SQLException e){
            System.out.println("Error - "+e.getMessage());
        }
    }
    static void viewJobpostings(){
        try {
            Connection connection = DriverManager.getConnection(DB_Url,userName,passWord);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("\nSELECT * FROM jobs");
            System.out.println("----All Job Posting-----");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("job_id")+"." +resultSet.getString("title")+" | Descriptions-"+resultSet.getString("description")+"| Status - "+resultSet.getInt("isApproved"));
            }

        }catch (SQLException e){
            System.out.println("Error "+e.getMessage());
        }
    }

}





















