package CA1;

public class Module {
    private final String modCode;
    private final String modName;
    private final int creditUnit;
    private final int mark;

    public Module(String modCode, String modName, int creditUnit, int mark) {
        this.modName = modName;
        this.modCode = modCode;
        this.creditUnit = creditUnit;
        this.mark = mark;
    }
    public String getModuleCodes() {
        return modCode;
    }

    public String getModuleNames() {
        return modName;
    }

    public int getMarks() {
        return mark;
    }

    public int getGradePoints() {
        if (mark >= 80) return 4;
        if (mark >= 70) return 3;
        if (mark >= 60) return 2;
        if (mark >= 50) return 1;
        return 0;
    }

    public char getGrade(){
        if (mark >= 80) return 'A';
        if (mark >= 70) return 'B';
        if (mark >= 60) return 'C';
        if (mark >= 50) return 'D';
        return 'F';
    }

    public int getCreditUnits() {
        return creditUnit;
    }


    @Override
    public String toString() {
        return "Module Code: " + modCode + ", " +
                "Module Name: " + modName + ", " +
                "Credit Unit: " + creditUnit + ", " +
                "Marks: " + mark;
    }
}