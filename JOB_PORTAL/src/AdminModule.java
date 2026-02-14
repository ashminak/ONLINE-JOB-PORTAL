import java.util.*;

class User{
    String username;
    String role;
    boolean isActive;

    public User(String role, String username ){
        this.role = role;
        this.username = username;
        this.isActive = false;
    }
}
class JobPosting{
    String title;
    String description;
    boolean isApprove;

    public JobPosting(String title, String description ){
        this.title = title;
        this.description = description ;
        this.isApprove = false;
    }
}

public class AdminModule {
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<JobPosting> jobs = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static void main(String[] args) {
        int choice =0;
        while (choice !=5){
            System.out.println("\n--- Admin Module ---");
            System.out.println("1. Add User");
            System.out.println("2. Approve Users");
            System.out.println("3. Add Job Posting");
            System.out.println("4. Approve Job Postings");
            System.out.println("5. Generate Reports / Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    addUser();
                    break;
                case 2:
                    approveUsers();
                    break;
                case 3:
                    addJobPosting();
                    break;
                case 4:
                    approveJobPosting();
                    break;
                case 5:
                    generateReports();
                    break;
                default:
                    System.out.println();


            }
        }

    }

    static void addUser(){
        System.out.println("Enter username: ");
        String username = sc.nextLine();
        System.out.println("Enter role(jobSeeker/employer): ");
        String role = sc.nextLine();
        users.add(new User(role, username));
        System.out.println("User added successfully! Awaiting admin approval.");
    }


    static void approveUsers(){
        System.out.println("\n---Pending Users---");
        int index = 0;
        for(User u:users){
            if (!u.isActive){
                System.out.println(index+". "+u.username+"("+u.role+")");
            }
            index++;
        }

        System.out.println("Enter Username to Approve ");
        String uname = sc.nextLine();
        boolean found = false;
        for (User u :users){
            if (u.username.equalsIgnoreCase(uname)){
                u.isActive =true;
                System.out.println("User Approved Successfully!");
                found =true;
                break;
            }
        }
        if (!found){
            System.out.println("User not found or already approved");
        }

    }

    static void addJobPosting(){
        System.out.println("Enter Job Title: ");
        String title = sc.nextLine();
        System.out.println("Enter Job Description: ");
        String desc = sc.nextLine();
        jobs.add(new JobPosting(title, desc));
        System.out.println("Job Posting added! Awaiting Admin approval.");
    }


    static void approveJobPosting() {
        System.out.println("\n---Pending Job Posting---");
        int index = 0;
        for (JobPosting j : jobs) {
            if (!j.isApprove) {
                System.out.println(index + ". " + j.title + "(" + j.description + ")");
            }
            index++;
        }
        System.out.println("Enter Job Posting to Approve: ");
        Scanner sc = new Scanner(System.in);
        String title= sc.nextLine();
        boolean found = false;
        for (JobPosting j : jobs){
            if (j.title.equalsIgnoreCase(title)){
                j.isApprove =true;
                System.out.println("Job approved Successfully!");
                found =true;
                break;
            }
        }
        if (!found){
            System.out.println("Jobs not found or already approved.");
        }
    }

    static void generateReports(){
        int activeUsers = 0;
        int pendingUsers = 0;
        int approvedJobs = 0;
        int pendingJobs = 0;

        for (User u : users){
            if (!u.isActive) {
                pendingUsers++;
            } else {
                activeUsers++;
            }
        }

        for (JobPosting j : jobs){
            if (!j.isApprove){
                approvedJobs++;
            } else {
                pendingJobs++;
            }
        }

        System.out.println("Total Users: "+users.size()+"| Active: "+activeUsers+"| Pending: "+pendingUsers);
        System.out.println("Total JobPostings: "+jobs.size()+"| Active: "+approvedJobs+"| Pending: "+pendingJobs);
    }

}

