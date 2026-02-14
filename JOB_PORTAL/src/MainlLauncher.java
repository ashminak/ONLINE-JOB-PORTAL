import java.sql.*;
import java.util.Scanner;
public class MainlLauncher {
    static Scanner sc = new Scanner(System.in);

    static void main(String[] args)throws ClassNotFoundException {
        int choice =0;
        while (choice!=4){
            System.out.println("----ONLINE JOB PORTAL----");
            System.out.println("1.Admin Portal");
            System.out.println("2.Job Seeker Portal");
            System.out.println("3.Employer Portal");
            System.out.println("4.Exit");
            System.out.println("Enter your choice : ");
            choice =sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    Admin.main(null);//launch Admin
                    break;
                case 2:
                    JobSeeker.main(null);//launch JobSeeker
                    break;
                case 3:
                    Employer.main(null);//launch Employer
                    break;
                case 4:
                    System.out.println("Exiting ONLINE JOB PORTAL.....GOODBYE.......");
                default:
                    System.out.println("Invalid choice! Please select 1-4.");
            }
        }
        sc.close();

    }
}
