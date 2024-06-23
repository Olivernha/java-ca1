package CA1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class StudentUser {
    private final StudentManagement sm;

    public StudentUser(StudentManagement sm) {
        this.sm = sm;
    }

    public void displayAllStudData() {
        String[] columnNames = {"Attribute", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        int count = 1;
        for (Student student : sm.getStudents()) {
            model.addRow(new Object[]{"Student No.", count++});
            model.addRow(new Object[]{"Name", student.getName()});
            model.addRow(new Object[]{"Admin No", student.getAdminNumber()});
            model.addRow(new Object[]{"Class", student.getStudentClass()});

            int module_no = 1;
            for (Module module : student.getModules()) {
                model.addRow(new Object[]{STR."Module \{module_no++}", STR."\{module.getModuleCodes()}/\{module.getModuleNames()}/\{module.getCreditUnits()}: \{module.getGrade()}"});
            }

            model.addRow(new Object[]{"", ""}); // Add an empty row for spacing
        }

         new InterfaceUtil(model,"All Student Data");
    }

    public void searchStudentByClass() {
        String studentClass = DialogUtil.getInput("Enter the class to search for (format: DIT/FT/2A/01):");
        double totalGpa = 0.0;
        if(studentClass == null){
            DialogUtil.showMessage("Invalid input. Input field cannot be empty!");
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9]{2,4}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
            DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
            return;
        }

        ArrayList<Student> studentsInClass = sm.findStudentsByClass(studentClass);
        if (studentsInClass.isEmpty()) {
            DialogUtil.showMessage("No students found from class");
            return;
        }

        for(Student student : studentsInClass){
            totalGpa += student.getGPA();
        }

        double avgGpa = totalGpa/studentsInClass.size();

        String message = STR."Number of Student(s) in \{studentClass}:\{studentsInClass.size()}\nAverage GPA : \{String.format("%.2f", avgGpa)}";

        DialogUtil.showMessage(message);
    }

    public void searchStudentByName() {
        String name = DialogUtil.getInput("Enter the name of the student to search for:");
        if (name == null || name.trim().isEmpty()) {
            DialogUtil.showMessage("Invalid input. Name cannot be empty.");
            return;
        }
        ArrayList<Student> studentsByName = sm.findStudentsByName(name);
        if (studentsByName.isEmpty()) {
            DialogUtil.showErrorMessage(STR."Cannot find the student\"\{name}\"!!");
            return;
        }

        String[] columnNames = {"Attribute", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (sm.isNotUnique(studentsByName)) {
            int dialogResult = DialogUtil.showConfirm(STR."\{studentsByName.size()} students found with this name \"\{name}\". Want to filter by admin number?", "Filter by Admin Number? (Y/N)");
            if (dialogResult == JOptionPane.NO_OPTION) {
                for (Student student : studentsByName) {
                    formatStudentDetails(student,model);
                }

            } else {
                String admNo = DialogUtil.getInput("Enter the admin number to filter:");
                if (admNo == null || admNo.trim().isEmpty()) {
                    DialogUtil.showMessage("Invalid input. Admin number cannot be empty.");
                    return;
                }

                Student student = sm.findStudentByAdminNumber(admNo);
                if (student != null) {
                    formatStudentDetails(student,model);
                } else {
                    DialogUtil.showMessage(STR."No student found with admin number \{admNo}");
                }
            }
        }
        else {
            for (Student student : studentsByName) {
                formatStudentDetails(student,model);
            }
        }

        new InterfaceUtil(model,"Student Details");
    }

    private void formatStudentDetails(Student student, DefaultTableModel model) {
        model.addRow(new Object[]{"Name", student.getName()});
        model.addRow(new Object[]{"Admin No", student.getAdminNumber()});
        model.addRow(new Object[]{"Class", student.getStudentClass()});

        int module_no = 1;
        for (Module module : student.getModules()) {
            model.addRow(new Object[]{STR."Module \{module_no++}", STR."\{module.getModuleCodes()}/\{module.getModuleNames()}/\{module.getCreditUnits()}: \{module.getGrade()}"});
        }
        model.addRow(new Object[]{"GPA",String.format("%.2f",student.getGPA())});

        model.addRow(new Object[]{"", ""}); // Add an empty row for spacing
    }

    public void displayMostDifficultModule() {
        Module module = sm.findMostDifficultModule();
        if (module != null) {
            DialogUtil.showMessage(STR."Most Difficult Module:\n\nModule code : \{module.getModuleCodes()}\nModule Name : \{module.getModuleNames()}\nModule Credit : \{module.getCreditUnits()}");
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }

    public void displayEasiestModule() {
        Module module = sm.findEasiestModule();
        if (module != null) {
            DialogUtil.showMessage(STR."Easiest Module:\n\nModule code : \{module.getModuleCodes()}\nModule Name : \{module.getModuleNames()}\nModule Credit : \{module.getCreditUnits()}");
        } else {
            DialogUtil.showMessage("No modules found.");
        }
    }
}
