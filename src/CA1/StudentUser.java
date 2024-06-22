package CA1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class StudentUser {
    private final StudentManagement sm;

    public StudentUser(StudentManagement sm) {
        this.sm = sm;
    }

    public void displayAllStudData() {
        StringBuilder display_text = new StringBuilder();
        int count = 1;
        for (Student student : sm.getStudents()) {
            display_text.append("Student ")
                    .append(count++)
                    .append(":\n")
                    .append("Name: ")
                    .append(student.getName())
                    .append("\nAdmin: ")
                    .append(student.getAdminNumber())
                    .append("\nClass: ")
                    .append(student.getStudentClass())
                    .append("\nModules Taken:\n")
                    .append(student.displayModules())
                    .append("\n")
                    .append("-----------------------------------\n");
        }
        DialogUtil.showMessage(display_text.toString());
    }

//    public void displayStudentGPA() {
//        String adminNumber = DialogUtil.getInput("Enter your admin number:");
//        Student student = sm.findStudentByAdminNumber(adminNumber);
//
//        if (student == null) {
//            DialogUtil.showMessage("Student not found.");
//            return;
//        }
//
//        double gpa = student.getGPA();
//        DialogUtil.showMessage("Your GPA: " + String.format("%.2f", gpa));
//    }
//
//    public void listModules() {
//        String adminNumber = DialogUtil.getInput("Enter your admin number:");
//        Student student = sm.findStudentByAdminNumber(adminNumber);
//
//        if (student == null) {
//            DialogUtil.showMessage("Student not found.");
//            return;
//        }
//
//        StringBuilder message = new StringBuilder("Your Modules:\n\n");
//        for (Module module : student.getModules()) {
//            message.append("Module Code: ").append(module.getModuleCodes()).append("\n")
//                    .append("Module Name: ").append(module.getModuleNames()).append("\n")
//                    .append("Credit Units: ").append(module.getCreditUnits()).append("\n")
//                    .append("Marks: ").append(module.getMarks()).append("\n")
//                    .append("Grade Points: ").append(module.getGradePoints()).append("\n\n");
//        }
//
//        DialogUtil.showMessage(message.toString());
//    }

    public void searchStudentByClass() {
        String studentClass = DialogUtil.getInput("Enter the class to search for (format: DIT/FT/2A/01):");
        if (studentClass == null) {
            DialogUtil.showMessage("no class entered");
            return;
        }
        if (!Pattern.matches("^[A-Za-z0-9]{3}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
            DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
            return;
        }

        ArrayList<Student> studentsInClass = sm.findStudentsByClass(studentClass);
        if (studentsInClass.isEmpty()) {
            DialogUtil.showMessage("No students found in class " + studentClass);
            return;
        }

        StringBuilder message = new StringBuilder("Students in class " + studentClass + ":\n\n");
        for (Student student : studentsInClass) {
            message.append("Name: ").append(student.getName()).append("\n")
                    .append("Admin Number: ").append(student.getAdminNumber()).append("\n")
                    .append("GPA: ").append(String.format("%.2f", student.getGPA())).append("\n\n");
        }

        DialogUtil.showMessage(message.toString());
    }

    public void searchStudentByName() {
        String name = DialogUtil.getInput("Enter the name of the student to search for:");
        if (name == null || name.trim().isEmpty()) {
            DialogUtil.showMessage("Invalid input. Name cannot be empty.");
            return;
        }
        ArrayList<Student> studentsByName = sm.findStudentsByName(name);
        if (studentsByName.isEmpty()) {
            DialogUtil.showMessage("No students found with the name " + name);
            return;
        }
        StringBuilder message = new StringBuilder();
        if (sm.isNotUnqiue(studentsByName)) {
            int dialogResult = DialogUtil.showConfirm("Multiple students found with the name " + name + ". Want to filter by admin number?", "Filter by Admin Number? (Y/N)");
            if (dialogResult == JOptionPane.NO_OPTION) {
                for (Student student : studentsByName) {
                    message.append(formatStudentDetails(student)).append("\n-------------------------\n");
                }
                DialogUtil.showMessage(message.toString());
            } else {
                String admNo = DialogUtil.getInput("Enter the admin number to filter:");
                if (admNo == null || admNo.trim().isEmpty()) {
                    DialogUtil.showMessage("Invalid input. Admin number cannot be empty.");
                    return;
                }

                Student student = sm.findStudentByAdminNumber(admNo);
                if (student != null) {
                    DialogUtil.showMessage(formatStudentDetails(student));
                } else {
                    DialogUtil.showMessage("No student found with admin number " + admNo);
                }
            }
        }
        else {
            for (Student student : studentsByName) {
                message.append(formatStudentDetails(student)).append("\n-------------------------\n");
            }
            DialogUtil.showMessage(message.toString());
        }
    }

    private String formatStudentDetails(Student student) {
        return "Name: " + student.getName() + "\n" +
                "Admin: " + student.getAdminNumber() + "\n" +
                "Class: " + student.getStudentClass() + "\n" +
                "Modules Taken:\n" + student.displayModules() + "\n" +
                "GPA: " + String.format("%.2f", student.getGPA()) + "\n";
    }

}