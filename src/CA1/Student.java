package CA1;

import java.util.ArrayList;

public class Student {
    private final String name;
    private final String admNo;
    private final String stdClass;
    private double gpa;
    private final ArrayList<Module> modules;

    public Student(String name, String admNo, String stdClass, ArrayList<Module> modules) {
        this.name = name;
        this.admNo = admNo;
        this.modules = modules;
        this.stdClass = stdClass;
        this.gpa = calculateGPA();
    }

    public String getName() {
        return name;
    }

    public String getAdminNumber() {
        return admNo;
    }

    public String getStudentClass() {
        return stdClass;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public double getGPA() {
        return gpa;
    }

    public void updateGPA() {
        this.gpa = calculateGPA();
    }

    public double calculateGPA() {
        int totalGradePoints = 0;
        int totalCreditUnits = 0;

        for (Module module : modules) {
            totalGradePoints += module.getGradePoints() * module.getCreditUnits();
            totalCreditUnits += module.getCreditUnits();
        }

        if (totalCreditUnits == 0) return 0.0;

        return (double) totalGradePoints / totalCreditUnits;
    }

    public String displayModules() {
        StringBuilder display_module = new StringBuilder();
        for (int i = 0; i < modules.size(); i++) {
            display_module.append((i + 1))
                    .append(".")
                    .append(modules.get(i).getModuleCodes())
                    .append("/")
                    .append(modules.get(i).getModuleNames())
                    .append("/")
                    .append(modules.get(i).getCreditUnits())
                    .append(": ")
                    .append(modules.get(i).getGrade())
                    .append("\n");
        }
        return display_module.toString();
    }

    public String getModulesAsString() {
        StringBuilder modulesStr = new StringBuilder();
        for (Module module : modules) {
            modulesStr.append(module.toString()).append("\n");
        }
        return modulesStr.toString();
    }
}