package CA1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class StudentAdmin {
    private final StudentManagement sm;

    public StudentAdmin(StudentManagement sm) {
        this.sm = sm;
    }

    public void addStudent() {
        String name = DialogUtil.getInput("Enter the student name:");
        String adminNumber = DialogUtil.getInput("Enter the admin number (start with p):");
        String studentClass = DialogUtil.getInput("Enter the class (format: DIT/FT/2A/01):");

        if (name == null || adminNumber == null || studentClass == null) return;

        if (!adminNumber.startsWith("p")) {
            DialogUtil.showMessage("Invalid admin number. Admin number should start with 'p'.");
            return;
        }
        if (!Pattern.matches("^[A-Za-z0-9]{3}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
            DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
            return;
        }

        int numModules;
        try {
            numModules = Integer.parseInt(DialogUtil.getInput("Enter the number of modules:"));
        } catch (NumberFormatException e) {
            DialogUtil.showMessage("Invalid number of modules.");
            return;
        }

        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < numModules; i++) {
            String moduleCode = DialogUtil.getInput("Enter module code:");
            String moduleName = DialogUtil.getInput("Enter module name:");
            int creditUnits;
            try {
                creditUnits = Integer.parseInt(DialogUtil.getInput("Enter credit units:"));
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid credit units.");
                return;
            }
            int marks;
            try {
                marks = Integer.parseInt(DialogUtil.getInput("Enter marks:"));
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid marks.");
                return;
            }
            modules.add(new Module(moduleCode, moduleName, creditUnits, marks));
        }

        sm.addStudent(new Student(name, adminNumber, studentClass, modules));
        DialogUtil.showMessage("Student created successfully!");
    }

    public void deleteStudent() {
        String adminNumber = DialogUtil.getInput("Enter the admin number of the student to delete:");
        if (adminNumber == null) return;

        if (sm.deleteStudent(adminNumber)) {
            DialogUtil.showMessage("Student deleted successfully!");
        } else {
            DialogUtil.showMessage("Student not found.");
        }
    }

    public void addModulesForStudent() {
        String adminNumber = DialogUtil.getInput("Enter the admin number of the student:");
        if (adminNumber == null) return;

        Student student = sm.findStudentByAdminNumber(adminNumber);
        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }

        boolean addMore = true;
        while (addMore) {
            String moduleCode = DialogUtil.getInput("Enter the module code:");
            String moduleName = DialogUtil.getInput("Enter the module name:");
            int creditUnit, marks;
            try {
                creditUnit = Integer.parseInt(DialogUtil.getInput("Enter the credit unit for module:"));
                marks = Integer.parseInt(DialogUtil.getInput("Enter the module marks for module:"));

                if (creditUnit < 0 || marks < 0) {
                    DialogUtil.showMessage("Invalid input. Credit unit and marks should be non-negative integers.");
                    return;
                }
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid input. Credit unit and marks should be integers.");
                return;
            }

            for (Module module : student.getModules()) {
                if (module.getModuleCodes().equals(moduleCode)) {
                    DialogUtil.showMessage("Module already exists for this student.");
                    return;
                }
            }

            Module module = new Module(moduleCode, moduleName, creditUnit, marks);
            student.getModules().add(module);
            DialogUtil.showMessage("Module added successfully.");

            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to add more modules?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.NO_OPTION) {
                addMore = false;
            }
        }
    }

    public void displayMostDifficultModule() {
        Module module = sm.findMostDifficultModule();
        if (module != null) {
            DialogUtil.showMessage("Most Difficult Module:\n\n" + module.toString());
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }

    public void displayEasiestModule() {
        Module module = sm.findEasiestModule();
        if (module != null) {
            DialogUtil.showMessage("Easiest Module:\n\n" + module.toString());
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }

    public void forecastGPA() {
        String adminNumber = DialogUtil.getInput("Enter the admin number of the student:");
        Student student = sm.findStudentByAdminNumber(adminNumber);
        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }

        double targetGPA;
        try {
            targetGPA = Double.parseDouble(DialogUtil.getInput("Enter the target GPA:"));
        } catch (NumberFormatException e) {
            DialogUtil.showMessage("Invalid GPA.");
            return;
        }

        int additionalModules;
        try {
            additionalModules = Integer.parseInt(DialogUtil.getInput("Enter the number of additional modules:"));
        } catch (NumberFormatException e) {
            DialogUtil.showMessage("Invalid number of modules.");
            return;
        }

        int creditUnitsPerModule;
        try {
            creditUnitsPerModule = Integer.parseInt(DialogUtil.getInput("Enter the credit units per module:"));
        } catch (NumberFormatException e) {
            DialogUtil.showMessage("Invalid credit units.");
            return;
        }

        int[] futureGrades = new int[additionalModules];
        for (int i = 0; i < additionalModules; i++) {
            try {
                futureGrades[i] = Integer.parseInt(DialogUtil.getInput("Enter grade for module " + (i + 1) + ":"));
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid grade.");
                return;
            }
        }

        boolean canAchieve = sm.canAchieveTargetGPA(student, targetGPA, additionalModules, creditUnitsPerModule, futureGrades);
        String message = "With the given future grades, " + (canAchieve ? "you can" : "you cannot") + " achieve the target GPA of " + targetGPA;
        DialogUtil.showMessage(message);
    }
}