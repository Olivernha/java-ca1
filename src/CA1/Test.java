package CA1;
import javax.swing.*;

public class Test {

    public static void main(String[] args) {
        // Create a frame (though not necessary for just an input dialog)
        JFrame frame = new JFrame("Input Dialog Example");

        // Show input dialog with a description
        String inputValue = JOptionPane.showInputDialog(frame,
                "Please enter your name:", "Input Dialog", JOptionPane.PLAIN_MESSAGE);

        // Handle the input value (null means user canceled or closed the dialog)
        if (inputValue != null) {
            JOptionPane.showMessageDialog(frame,
                    "Hello, " + inputValue + "!", "Greetings", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "You canceled or closed the dialog.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        // Optional: Set frame properties and make it visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
}