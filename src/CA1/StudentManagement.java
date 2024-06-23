/*
 * Name : LIN HTET & NAY HTET AUNG
 * ADMIN NO : p2340304 & p2340391
 * CLASS : DIT/FT/2A/03
 * */

package CA1;

import java.util.*;
import java.util.regex.Pattern;

public class StudentManagement {
    private final ArrayList<Student> students = new ArrayList<>();

    public StudentManagement() {
        ArrayList<Module> modules = new ArrayList<>();
        modules.add(new Module("ST00001", "Database", 4, 71));
        modules.add(new Module("ST00002", "Fundamental Of Programming", 4, 60));
        modules.add(new Module("ST00003", "Object Oriented Programming", 4, 63));
        addStudent(new Student("oliver", "p2340391", "DIT/FT/2A/01", modules));

        modules = new ArrayList<>();
        modules.add(new Module("ST00001", "Database", 4, 71));
        modules.add(new Module("ST00002", "Fundamental Of Programming", 4, 50));
        modules.add(new Module("ST00003", "Object Oriented Programming", 4, 90));
        addStudent(new Student("ST00004", "p2340390", "DIT/FT/2A/01", modules));
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
        return student_names.size() > 1;
    }

    public ArrayList<Student> findStuByDiplomaCode(String diploma_code){
        ArrayList<Student> student_info_by_diploma_code = new ArrayList<>();

        for(Student student : students){
            String split_diploma_code = student.getStudentClass().split("/",2)[0];
            if(split_diploma_code.equals(diploma_code)){
                student_info_by_diploma_code.add(student);
            }
        }
        return  student_info_by_diploma_code;
    }

    public Module findMostDifficultModule() {
        String diploma_code = DialogUtil.getInput("Choose Diploma to find most difficult module in. (e.g. DIT/DISM)");
        ArrayList<Student> filtered_stud_info_array = findStuByDiplomaCode(diploma_code);
        Map<String, int[]> moduleStats = new HashMap<>(); // [totalMarks, count]
        for (Student student : filtered_stud_info_array) {
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
        String diploma_code = DialogUtil.getInput("Enter diploma to find easiest module in (e.g. DIT/DISM)");
        ArrayList<Student> filtered_stud_info_array = findStuByDiplomaCode(diploma_code);
        Map<String, int[]> moduleStats = new HashMap<>(); // [totalMarks, count]
        for (Student student : filtered_stud_info_array) {
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

    public boolean canAchieveTargetGPA(Student student, double targetGPA, int additionalModules, int[] creditUnitsPerModule, int[] futureGrades) {
        int currentTotalGradePoints = 0;
        int currentTotalCreditUnits = 0;

        for (Module module : student.getModules()) {
            currentTotalGradePoints += module.getGradePoints() * module.getCreditUnits();
            currentTotalCreditUnits += module.getCreditUnits();
        }

        int futureTotalGradePoints = 0;
        int futureTotalCreditUnits = 0;

        for (int i = 0; i < futureGrades.length; i++) {
            futureTotalGradePoints += futureGrades[i] * creditUnitsPerModule[i];
            futureTotalCreditUnits += creditUnitsPerModule[i];
        }

        double futureGPA = (double) futureTotalGradePoints / futureTotalCreditUnits;
        return futureGPA >= targetGPA && currentTotalCreditUnits + additionalModules <= futureTotalCreditUnits;
    }
    public double calculateMedianGPA() {
        List<Double> gpas = new ArrayList<>();
        for (Student student : getStudents()) {
            gpas.add(student.getGPA());
        }
        gpas.sort(Comparator.naturalOrder());
        int size = gpas.size();
        if (size % 2 == 0) {
            return (gpas.get(size / 2 - 1) + gpas.get(size / 2)) / 2;
        } else {
            return gpas.get(size / 2);
        }
    }

    public double calculateHighestGPA() {
        double highestGPA = 0;
        for (Student student : getStudents()) {
            double gpa = student.getGPA();
            if (gpa > highestGPA) {
                highestGPA = gpa;
            }
        }
        return highestGPA;

    }

    public double calculateLowestGPA() {

        double lowestGPA = Double.MAX_VALUE;
        for (Student student : getStudents()) {
            double gpa = student.getGPA();
            if (gpa < lowestGPA) {
                lowestGPA = gpa;
            }
        }
        return lowestGPA;
    }

    public long countStudentsAboveGPAThreshold(double threshold) {
        int count = 0;
        for (Student student : getStudents()) {
            if (student.getGPA() > threshold) {
                count++;
            }
        }
        return count;
    }

    public String validateClassInput() {
        String student_class;
        while (true) {
            student_class = DialogUtil.getInput("Enter the class to search for (format: DIT/FT/2A/01):");

            if (student_class == null) {
                return null;
            }

            if (student_class.trim().isEmpty()) {
                DialogUtil.showMessage("Invalid input. Input field cannot be empty!");
                continue;
            }

            if (!Pattern.matches("^[A-Za-z0-9]{2,4}/[A-Za-z]{2}/\\d[A-Za-z]/\\d{2}$", student_class)) {
                DialogUtil.showMessage("Invalid class format. Please use the format DIT/FT/2A/01.");
                continue;
            }
            break;
        }
        return student_class;
    }

    public String validateStudentName() {
        String student_name;
        while (true) {
            student_name = DialogUtil.getInput("Enter the name of the student to search for:");

            if(student_name == null){
                return null;
            }

            if(student_name.trim().isEmpty()){
                DialogUtil.showMessage("Invalid input. Name cannot be empty.");
                continue;
            }
            break;
        }
        return student_name;
    }
}

