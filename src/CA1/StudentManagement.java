package CA1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentManagement {
    private final ArrayList<Student> students = new ArrayList<>();

    public StudentManagement() {
        ArrayList<Module> modules = new ArrayList<>();
        modules.add(new Module("DBScode", "DBS", 5, 80));
        addStudent(new Student("John Doe", "p2340390", "DIT/FT/2A/01", modules));
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public boolean deleteStudent(String adminNumber) {
        Student student = findStudentByAdminNumber(adminNumber);
        if (student != null) {
            students.remove(student);
            return true;
        }
        return false;
    }
    public boolean isNotUnqiue(ArrayList<Student> student_names){
        if(student_names.size() > 1){
            return true;
        }
        return false;
    }
    public Student findStudentByAdminNumber(String adminNumber) {
        for (Student student : students) {
            if (student.getAdminNumber().equals(adminNumber)) {
                return student;
            }
        }
        return null;
    }

    public ArrayList<Student> findStudentsByClass(String studentClass) {
        ArrayList<Student> studentsInClass = new ArrayList<>();
        for (Student student : students) {
            if (student.getStudentClass().equals(studentClass)) {
                studentsInClass.add(student);
            }
        }
        return studentsInClass;
    }
    public Module findModuleByCode(String moduleCode) {
        for (Student student : getStudents()) {
            for (Module module : student.getModules()) {
                if (module.getModuleCodes().equals(moduleCode)) {
                    return module;
                }
            }
        }
        return null;
    }
    public ArrayList<Student> findStudentsByName(String name) {
        ArrayList<Student> studentsByName = new ArrayList<>();

        for (Student student : students) {
            if (name.equalsIgnoreCase(student.getName())) {
                studentsByName.add(student);
            }
        }
        return studentsByName;
    }


    public boolean isNotUnique(ArrayList<Student> student_names){
        if(student_names.size() > 1){
            return true;
        }
        return false;
    }

    public Module findMostDifficultModule() {
        Map<String, int[]> moduleStats = new HashMap<>(); // [totalMarks, count]
        for (Student student : students) {
            for (Module module : student.getModules()) {
                moduleStats.computeIfAbsent(module.getModuleCodes(), k -> new int[2]);
                moduleStats.get(module.getModuleCodes())[0] += module.getMarks();
                moduleStats.get(module.getModuleCodes())[1]++;
            }
        }

        String mostDifficultModuleCode = null;
        double lowestAverage = Double.MAX_VALUE;

        for (Map.Entry<String, int[]> entry : moduleStats.entrySet()) {
            double average = (double) entry.getValue()[0] / entry.getValue()[1];
            if (average < lowestAverage) {
                lowestAverage = average;
                mostDifficultModuleCode = entry.getKey();
            }
        }

        return getModuleByCode(mostDifficultModuleCode);
    }

    public Module findEasiestModule() {
        Map<String, int[]> moduleStats = new HashMap<>(); // [totalMarks, count]
        for (Student student : students) {
            for (Module module : student.getModules()) {
                moduleStats.computeIfAbsent(module.getModuleCodes(), k -> new int[2]);
                moduleStats.get(module.getModuleCodes())[0] += module.getMarks();
                moduleStats.get(module.getModuleCodes())[1]++;
            }
        }

        String easiestModuleCode = null;
        double highestAverage = Double.MIN_VALUE;

        for (Map.Entry<String, int[]> entry : moduleStats.entrySet()) {
            double average = (double) entry.getValue()[0] / entry.getValue()[1];
            if (average > highestAverage) {
                highestAverage = average;
                easiestModuleCode = entry.getKey();
            }
        }

        return getModuleByCode(easiestModuleCode);
    }

    private Module getModuleByCode(String moduleCode) {
        for (Student student : students) {
            for (Module module : student.getModules()) {
                if (module.getModuleCodes().equals(moduleCode)) {
                    return module;
                }
            }
        }
        return null;
    }

    public boolean canAchieveTargetGPA(Student student, double targetGPA, int additionalModules, int creditUnitsPerModule, int[] futureGrades) {
        int currentTotalGradePoints = 0;
        int currentTotalCreditUnits = 0;

        for (Module module : student.getModules()) {
            currentTotalGradePoints += module.getGradePoints() * module.getCreditUnits();
            currentTotalCreditUnits += module.getCreditUnits();
        }

        int futureTotalGradePoints = 0;
        for (int grade : futureGrades) {
            futureTotalGradePoints += grade * creditUnitsPerModule;
        }

        int totalGradePoints = currentTotalGradePoints + futureTotalGradePoints;
        int totalCreditUnits = currentTotalCreditUnits + (additionalModules * creditUnitsPerModule);

        double projectedGPA = (double) totalGradePoints / totalCreditUnits;

        return projectedGPA >= targetGPA;
    }
}