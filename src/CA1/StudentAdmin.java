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
        String name = getValidStringInput("Enter the student name:");
        String adminNumber = getUniqueAdminNumber();
        String studentClass = getValidStudentClass();
        int numModules = getValidIntegerInput("Enter the number of modules:", 0, Integer.MAX_VALUE);

        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < numModules; i++) {
            modules.add(createModule(modules));
        }

        sm.addStudent(new Student(name, adminNumber, studentClass, modules));
        DialogUtil.showMessage("Student created successfully!");
    }

    public void deleteStudent() {
        String adminNumber = getValidStringInput("Enter the admin number of the student to delete:");
        if (sm.deleteStudent(adminNumber)) {
            DialogUtil.showMessage("Student deleted successfully!");
        } else {
            DialogUtil.showMessage("Student not found.");
        }
    }

    public void addModulesForStudent() {
        String adminNumber = getValidStringInput("Enter the admin number of the student:");
        Student student = sm.findStudentByAdminNumber(adminNumber);

        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }

        boolean addMore = true;
        while (addMore) {
            student.getModules().add(createModule(student.getModules()));
            student.updateGPA();
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to add more modules?", "Warning", JOptionPane.YES_NO_OPTION);
            addMore = dialogResult == JOptionPane.YES_OPTION;
        }
    }

    public void displayMostDifficultModule() {
        Module module = sm.findMostDifficultModule();
        if (module != null) {
            DialogUtil.showMessage("Most Difficult Module:\n\n" + module);
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }

    public void displayEasiestModule() {
        Module module = sm.findEasiestModule();
        if (module != null) {
            DialogUtil.showMessage("Easiest Module:\n\n" + module);
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }

    public void forecastGPA() {
        String adminNumber = getValidStringInput("Enter the admin number of the student:");
        Student student = sm.findStudentByAdminNumber(adminNumber);

        if (student == null) {
            DialogUtil.showMessage("Student not found.");
            return;
        }

        double targetGPA = getValidDoubleInput("Enter the target GPA:", 0, 4);
        int additionalModules = getValidIntegerInput("Enter the number of additional modules:", 0, Integer.MAX_VALUE);
        int creditUnitsPerModule = getValidIntegerInput("Enter the credit units per module:", 0, Integer.MAX_VALUE);

        int[] futureGrades = new int[additionalModules];
        for (int i = 0; i < additionalModules; i++) {
            futureGrades[i] = getValidIntegerInput("Enter grade for module " + (i + 1) + ":", 0, 100);
        }

        boolean canAchieve = sm.canAchieveTargetGPA(student, targetGPA, additionalModules, creditUnitsPerModule, futureGrades);
        String message = "With the given future grades, " + (canAchieve ? "you can" : "you cannot") + " achieve the target GPA of " + targetGPA;
        DialogUtil.showMessage(message);
    }

    private Module createModule(ArrayList<Module> existingModules) {
        String moduleCode = getUniqueModuleCode(existingModules);
        Module existingModule = sm.findModuleByCode(moduleCode);

        if (existingModule != null) {
            DialogUtil.showMessage("This module code already exists in the system. Automatically added with module name: " + existingModule.getModuleNames() + " and credit units: " + existingModule.getCreditUnits());
            int marks = getValidIntegerInput("Enter marks:", 0, 100);
            return new Module(moduleCode, existingModule.getModuleNames(), existingModule.getCreditUnits(), marks);
        } else {
            String moduleName = getUniqueModuleName();
            int creditUnits = getValidIntegerInput("Enter credit units:", 0, Integer.MAX_VALUE);
            int marks = getValidIntegerInput("Enter marks:", 0, 100);
            return new Module(moduleCode, moduleName, creditUnits, marks);
        }
    }

    private String getUniqueModuleName() {
        while (true) {
            String moduleName = getValidStringInput("Enter module name:");
            if (!moduleNameExists(moduleName)) {
                return moduleName;
            }
            DialogUtil.showMessage("Module name already exists in the system. Please enter a different module name.");
        }
    }

    private boolean moduleNameExists(String moduleName) {
        for (Student s : sm.getStudents()) {
            for (Module module : s.getModules()) {
                if (module.getModuleNames().equalsIgnoreCase(moduleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getUniqueModuleCode(ArrayList<Module> modules) {
        while (true) {
            String moduleCode = getValidStringInput("Enter module code:");
            if (!moduleCodeExists(moduleCode, modules)) {
                return moduleCode;
            }
            DialogUtil.showMessage("Module code already added. Please enter a different module code.");
        }
    }

    private boolean moduleCodeExists(String moduleCode, ArrayList<Module> modules) {
        for (Module module : modules) {
            if (module.getModuleCodes().equals(moduleCode)) {
                return true;
            }
        }
        return false;
    }



    private String getValidStringInput(String message) {
        while (true) {
            String input = DialogUtil.getInput(message);
            if (input != null && !input.trim().isEmpty()) {
                return input;
            }
            DialogUtil.showMessage("Invalid input. Please enter a non-empty string.");
        }
    }

    private String getValidAdminNumber() {
        while (true) {
            String adminNumber = DialogUtil.getInput("Enter the admin number (start with p):");
            if (adminNumber != null && adminNumber.startsWith("p")) {
                return adminNumber;
            }
            DialogUtil.showMessage("Invalid admin number. Admin number should start with 'p'.");
        }
    }

    private String getUniqueAdminNumber() {
        while (true) {
            String adminNumber = getValidAdminNumber();
            if (sm.findStudentByAdminNumber(adminNumber) == null) {
                return adminNumber;
            }
            DialogUtil.showMessage("Admin number already exists. Please enter a different admin number.");
        }
    }

    private String getValidStudentClass() {
        while (true) {
            String studentClass = DialogUtil.getInput("Enter the class (format: DIT/FT/2A/01):");
            if (studentClass != null && Pattern.matches("^[A-Za-z0-9]{3}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
                return studentClass;
            }
            DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
        }
    }

    private int getValidIntegerInput(String message, int minValue, int maxValue) {
        while (true) {
            try {
                int input = Integer.parseInt(DialogUtil.getInput(message));
                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    DialogUtil.showMessage("Invalid input. Please enter a value between " + minValue + " and " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private double getValidDoubleInput(String message, double minValue, double maxValue) {
        while (true) {
            try {
                double input = Double.parseDouble(DialogUtil.getInput(message));
                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    DialogUtil.showMessage("Invalid input. Please enter a value between " + minValue + " and " + maxValue + ".");
                }
            } catch (NumberFormatException e) {
                DialogUtil.showMessage("Invalid input. Please enter a valid double.");
            }
        }
    }
}
