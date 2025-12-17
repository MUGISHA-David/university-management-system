package universitymanagementsystem;

import java.util.ArrayList;
import java.util.Scanner;

public class UniversityManagementSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<String> students = new ArrayList<>();
    static ArrayList<String> lecturers = new ArrayList<>();
    static ArrayList<String> courses = new ArrayList<>();

    public static void main(String[] args) {

        int choice;

        System.out.println("===================================");
        System.out.println("   UNIVERSITY MANAGEMENT SYSTEM   ");
        System.out.println("===================================");

        do {
            System.out.println("\n1. Student Management");
            System.out.println("2. Lecturer Management");
            System.out.println("3. Course Management");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    studentMenu();
                    break;
                case 2:
                    lecturerMenu();
                    break;
                case 3:
                    courseMenu();
                    break;
                case 4:
                    System.out.println("Exiting system... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 4);
    }

    // ================= STUDENT MENU =================
    static void studentMenu() {
        int choice;
        do {
            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Delete Student");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter student name: ");
                    students.add(sc.nextLine());
                    System.out.println("Student added successfully!");
                    break;

                case 2:
                    displayList(students, "Students");
                    break;

                case 3:
                    deleteItem(students, "Student");
                    break;
            }
        } while (choice != 4);
    }

    // ================= LECTURER MENU =================
    static void lecturerMenu() {
        int choice;
        do {
            System.out.println("\n--- LECTURER MANAGEMENT ---");
            System.out.println("1. Add Lecturer");
            System.out.println("2. View Lecturers");
            System.out.println("3. Delete Lecturer");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter lecturer name: ");
                    lecturers.add(sc.nextLine());
                    System.out.println("Lecturer added successfully!");
                    break;

                case 2:
                    displayList(lecturers, "Lecturers");
                    break;

                case 3:
                    deleteItem(lecturers, "Lecturer");
                    break;
            }
        } while (choice != 4);
    }

    // ================= COURSE MENU =================
    static void courseMenu() {
        int choice;
        do {
            System.out.println("\n--- COURSE MANAGEMENT ---");
            System.out.println("1. Add Course");
            System.out.println("2. View Courses");
            System.out.println("3. Delete Course");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter course name: ");
                    courses.add(sc.nextLine());
                    System.out.println("Course added successfully!");
                    break;

                case 2:
                    displayList(courses, "Courses");
                    break;

                case 3:
                    deleteItem(courses, "Course");
                    break;
            }
        } while (choice != 4);
    }

    // ================= COMMON METHODS =================
    static void displayList(ArrayList<String> list, String title) {
        if (list.isEmpty()) {
            System.out.println(title + " list is empty.");
        } else {
            System.out.println("\n" + title + " List:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
    }

    static void deleteItem(ArrayList<String> list, String name) {
        if (list.isEmpty()) {
            System.out.println("No " + name + " to delete.");
            return;
        }

        displayList(list, name + "s");
        System.out.print("Enter number to delete: ");
        int index = sc.nextInt();

        if (index > 0 && index <= list.size()) {
            list.remove(index - 1);
            System.out.println(name + " deleted successfully!");
        } else {
            System.out.println("Invalid selection!");
        }
    }
}
