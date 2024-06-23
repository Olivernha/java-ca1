package CA1;

import javax.swing.*;

public class App {
    static StudentManagement sm = new StudentManagement();
    static StudentAdmin admin = new StudentAdmin(sm);
    static StudentUser studentUser = new StudentUser(sm);

    public static void main(String[] args) {
        showMainMenu();
    }

    public static void showMainMenu() {
        while (true) {
            String[] options = {"1. Admin System", "2. Enquiry System", "3. Exit"};
            String choice = DialogUtil.getChoice("Choose an option:", options, "Main Menu");

            if (choice == null) return;

            switch (choice) {
                case "1. Admin System":
                    showAdminMenu();
                    break;
                case "2. Enquiry System":
                    showStudentMenu();
                    break;
                case "3. Exit":
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    System.exit(0);
            }
        }
    }

    private static void showAdminMenu() {
        while (true) {
            String[] options = {
                    "1. Create Student", "2. Delete Student", "3. Add Module for Student",
                    "4. Show Statistics",
                    "5. GPA Forecast", "6. Back to Main Menu", "7. Exit"
            };
            String choice = DialogUtil.getChoice("Choose an option:", options, "Admin Menu");

            if (choice == null) return;

            switch (AdminMenuOption.values()[Integer.parseInt(choice.split("\\.")[0]) - 1]) {
                case CREATE_STUDENT:
                    admin.addStudent();
                    break;
                case DELETE_STUDENT:
                    admin.deleteStudent();
                    break;
                case ADD_MODULE:
                    admin.addModulesForStudent();
                    break;
                case SHOW_STATISTICS:
                   admin.displayStatistics();
                    break;
                case GPA_FORECAST:
                    admin.forecastGPA();
                    break;
                case BACK_TO_MAIN:
                    return;
                case QUIT:
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    System.exit(0);
            }
        }
    }

    private static void showStudentMenu() {
        while (true) {
            String[] options = {
                    "1. Display All Students", "2. Search student by class",
                    "3. Search student by name", "4. Back to Main Menu", "5. Quit"
            };
            String choice = DialogUtil.getChoice("Choose an option:", options, "Student Menu");

            if (choice == null) return;

            switch (StudentMenuOption.values()[Integer.parseInt(choice.split("\\.")[0]) - 1]) {
                case DISPLAY_ALL:
                    studentUser.displayAllStudData();
                    break;
                case SEARCH_BY_CLASS:
                    studentUser.searchStudentByClass();
                    break;
                case SEARCH_BY_NAME:
                    studentUser.searchStudentByName();
                    break;
                case BACK_TO_MAIN:
                    return;
                case QUIT:
                    DialogUtil.showMessage("Thank you for using the Student Management System!");
                    System.exit(0);
            }
        }
    }

}
