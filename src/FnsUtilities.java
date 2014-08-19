/**
 * @author Jethro Muller
 * @version 1.0.0
 *
 */
import javax.swing.JOptionPane;

public class FnsUtilities {
    private FnsGui gui = null;

    public FnsUtilities(FnsGui gui) {
        this.gui = gui;
    }

    /**
     * Displays the about dialog.
     */
    public void aboutDialog() {
        JOptionPane
                .showMessageDialog(
                        gui,
                        "About\n\nThis application calculates the values for the five number summary, it also calculates the Sum and Average.\n\n"
                        + "This was made in a few hours for fun on a public holiday.\n"
                        + "Five Number Summary Calculator v1.0\n\n"
                        + "Made by Jethro Muller\n" + "September 2013",
                        "About", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays the help dialog.
     */
    public void helpDialog() {
        JOptionPane
                .showMessageDialog(
                        gui,
                        "Welcome to Five Number Summary Calculator\n\n"
                        + " - To add numbers click in the textbox, type in a number and click Add or push Enter.\n"
                        + " - To view the Five Number Summary(min, Q2, median, Q3, max), click the Five Number Summary button.\n"
                        + " - To view the other information(sum, mean, standard deviation) click the Other Information button.\n"
                        + " - To undo your last Add click Edit > Undo Add.\n"
                        + " - To sort the data click Edit > Sort Data.\n"
                        + " - To clear the data, click Edit > Clear Data."
                        + " - To save the data you have entered as a text file (.txt) click File > Save."
                        + " - To open a text file(.txt) to import data click \n" +
                        " File > Open and then choose the appropriate file.",
                        "Help", JOptionPane.PLAIN_MESSAGE);

    }
}
