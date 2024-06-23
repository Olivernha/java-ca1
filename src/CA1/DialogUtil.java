package CA1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// Utility class for common dialog operations
class DialogUtil {
    static String getInput(String message) {
        return JOptionPane.showInputDialog(message);
    }

    static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    static void showErrorMessage(String message){JOptionPane.showMessageDialog(null,message,"Info",JOptionPane.ERROR_MESSAGE);}

    static String getChoice(String message, String[] options, String title) {
        return (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    static int showConfirm(String message, String title) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    }
}

// Enums for Admin and Student menu options
enum AdminMenuOption {
    CREATE_STUDENT, DELETE_STUDENT, ADD_MODULE, DISPLAY_MOST_DIFFICULT, DISPLAY_EASIEST, GPA_FORECAST, BACK_TO_MAIN, QUIT
}

enum StudentMenuOption {
    DISPLAY_ALL, SEARCH_BY_CLASS, SEARCH_BY_NAME, BACK_TO_MAIN, QUIT
}