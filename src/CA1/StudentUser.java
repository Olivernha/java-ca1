package CA1;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
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
                model.addRow(new Object[]{"Module "+ module_no++ , module.getModuleCodes() + "/" + module.getModuleNames() + "/" + module.getCreditUnits() + ": " + module.getGrade()});
            }

            model.addRow(new Object[]{"", ""}); // Add an empty row for spacing
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Setting Row Colour
        table.setDefaultRenderer(Object.class, new TableInterfaceUtil());

        // Setting Heading colour
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderInterfaceUtil());

        JOptionPane.showMessageDialog(null, scrollPane, "All Students Data", JOptionPane.PLAIN_MESSAGE);
    }

    public void searchStudentByClass() {
        String studentClass = DialogUtil.getInput("Enter the class to search for (format: DIT/FT/2A/01):");

        double totalGpa = 0.0;
        if(studentClass == null){
            DialogUtil.showMessage("Invalid input. Input field cannot be empty!");
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9]{3,4}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", studentClass)) {
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

        StringBuilder message = new StringBuilder();
        message.append("Number of Student(s) in ")
                .append(studentClass)
                .append(":")
                .append(studentsInClass.size())
                .append("\n")
                .append("Average GPA : ")
                .append(String.format("%.2f", avgGpa));
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
            DialogUtil.showErrorMessage("Cannot find the student" + "\"" + name + "\"!!");
            return;
        }

//        StringBuilder message = new StringBuilder();
        String[] columnNames = {"Attribute", "Value"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (sm.isNotUnique(studentsByName)) {
            int dialogResult = DialogUtil.showConfirm(studentsByName.size() +" students found with this name " + "\"" + name + "\"" + ". Want to filter by admin number?", "Filter by Admin Number? (Y/N)");
            if (dialogResult == JOptionPane.NO_OPTION) {
                for (Student student : studentsByName) {
                    formatStudentDetails(student,model);
                }
//                DialogUtil.showMessage(message.toString());
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
                    DialogUtil.showMessage("No student found with admin number " + admNo);
                }
            }
        }
        else {
            for (Student student : studentsByName) {
                formatStudentDetails(student,model);
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Setting Row Colour
        table.setDefaultRenderer(Object.class, new TableInterfaceUtil());

        // Setting Heading colour
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderInterfaceUtil());

        JOptionPane.showMessageDialog(null, scrollPane, "All Students Data", JOptionPane.PLAIN_MESSAGE);
    }

    private void formatStudentDetails(Student student, DefaultTableModel model) {
        model.addRow(new Object[]{"Name", student.getName()});
        model.addRow(new Object[]{"Admin No", student.getAdminNumber()});
        model.addRow(new Object[]{"Class", student.getStudentClass()});

        int module_no = 1;
        for (Module module : student.getModules()) {
            model.addRow(new Object[]{"Module "+ module_no++ , module.getModuleCodes() + "/" + module.getModuleNames() + "/" + module.getCreditUnits() + ": " + module.getGrade()});
        }
        model.addRow(new Object[]{"GPA",String.format("%.2f",student.getGPA())});

        model.addRow(new Object[]{"", ""}); // Add an empty row for spacing
    }
}

class TableInterfaceUtil extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == 0) {
            component.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
        }
        return component;
    }
}

class HeaderInterfaceUtil extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setBackground(Color.BLACK);
        c.setForeground(Color.WHITE);
        c.setFont(new Font("Arial", Font.BOLD, 15));


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