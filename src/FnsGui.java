/**
 * @author Jethro Muller
 * @version 1.0.0
 *
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class FnsGui extends JFrame implements ActionListener, KeyListener, WindowListener {
    private final Font displayFont = new Font("Arial", Font.BOLD, 12);
    private final String displayHeading = "  Data:\n=======================";
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Dimension dim = toolkit.getScreenSize();
    private int screenWidth = (int) (dim.width * 0.75);
    private int screenHeight = (int) (dim.height * 0.75);
    private final Dimension windowDimensions = new Dimension(screenWidth, screenHeight);
    private int count = 0;
    private double sum = 0;

    private double median;
    private double firstQuartile;
    private double thirdQuartile;
    private double max;
    private double min;

    private double lowerOutlier = 0;
    private double upperOutlier = 0;

    private double lowerStray = 0;
    private double upperStray = 0;

    ArrayList<Double> strayList = new ArrayList<Double>();
    ArrayList<Double> outlierList = new ArrayList<Double>();

    private JLabel addLabel = null;
    private JTextField dataField = null;

    private JTextArea display = null;
    private JScrollPane displayScrollPane = null;

    private JMenu fileMenu = null;

    private JMenu editMenu = null;

    private JMenu helpMenu = null;

    private JButton addButton = null;
    private JButton fiveNumberSummaryButton = null;
    private JButton generalInfoButton = null;
    private JButton straysAndOutlierButton = null;

    private List<Double> data = null;

    private FnsUtilities utility = null;
    private boolean recentlySaved = true;

    /**
     * Parametrized constructor.
     */
    public FnsGui() {
        utility = new FnsUtilities(this);
        createJFrame();
        this.addWindowListener(this);
        createFileMenu();
        createEditMenu();
        createHelpMenu();

        data = new ArrayList<Double>();

        JMenuBar topMenuBar = new JMenuBar();
        topMenuBar.add(fileMenu);
        topMenuBar.add(editMenu);
        topMenuBar.add(helpMenu);

        this.setJMenuBar(topMenuBar);

        createMainFeatures();
        addComponents();
    }

    /**
     * Initializes the main JFrame and changes all necessary settings.
     */
    private void createJFrame() {
        setTitle("Five Number Summary Calculator");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(windowDimensions);
        pack();
        addKeyListener(this);
        setLayout(new FlowLayout());
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Initializes and sets everything needed for the File menu.
     */
    private void createFileMenu() {
        fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        openMenuItem.addActionListener(this);
        openMenuItem.setActionCommand("open");

        saveMenuItem.addActionListener(this);
        saveMenuItem.setActionCommand("save");

        quitMenuItem.addActionListener(this);
        quitMenuItem.setActionCommand("quit");

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(quitMenuItem);
    }

    /**
     * Initializes and sets everything needed for the Edit menu.
     */
    private void createEditMenu() {
        editMenu = new JMenu("Edit");
        JMenuItem sortMenuItem = new JMenuItem("Sort Data");
        JMenuItem undoAddMenuItem = new JMenuItem("Undo Add");
        JMenuItem clearMenuItem = new JMenuItem("Clear Data");

        sortMenuItem.addActionListener(this);
        sortMenuItem.setActionCommand("sort");

        undoAddMenuItem.addActionListener(this);
        undoAddMenuItem.setActionCommand("undo");

        clearMenuItem.addActionListener(this);
        clearMenuItem.setActionCommand("clear");

        editMenu.add(sortMenuItem);
        editMenu.add(undoAddMenuItem);
        editMenu.add(clearMenuItem);
    }

    /**
     * Initializes and sets everything needed for the Help menu.
     */
    private void createHelpMenu() {
        helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);
        aboutMenuItem.setActionCommand("about");

        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(this);
        helpMenuItem.setActionCommand("help");

        helpMenu.add(aboutMenuItem);
        helpMenu.add(helpMenuItem);
    }

    /**
     * Initialises and sets everything needed for the main program.
     */
    private void createMainFeatures() {
        addLabel = new JLabel("Data: ");

        dataField = new JTextField();
        dataField.addKeyListener(this);
        addLabel.setLabelFor(dataField);
        dataField.setPreferredSize(new Dimension(100, 25));

        addButton = new JButton("Add");
        addButton.setActionCommand("add");
        addButton.setPreferredSize(new Dimension(100, 25));
        addButton.addActionListener(this);

        fiveNumberSummaryButton = new JButton("Five Number Summary");
        fiveNumberSummaryButton.setActionCommand("summary");
        fiveNumberSummaryButton.setPreferredSize(new Dimension(200, 25));
        fiveNumberSummaryButton.addActionListener(this);

        generalInfoButton = new JButton("General Info");
        generalInfoButton.setActionCommand("general");
        generalInfoButton.setPreferredSize(new Dimension(150, 25));
        generalInfoButton.addActionListener(this);

        straysAndOutlierButton = new JButton("Strays and Outliers");
        straysAndOutlierButton.setActionCommand("strayandoutlier");
        straysAndOutlierButton.setPreferredSize(new Dimension(150, 25));
        straysAndOutlierButton.addActionListener(this);

        display = new JTextArea(displayHeading);
        display.setBorder(new LineBorder(Color.BLACK, 2, true));
        display.setFont(displayFont);
        display.setEditable(false);
        display.setFocusable(false);

        displayScrollPane = new JScrollPane(display);
        int scrollWidth = (int) (screenWidth * 0.9);
        int scrollHeight = (int) (screenHeight * 0.7);
        displayScrollPane.setPreferredSize(new Dimension(scrollWidth, scrollHeight));
    }

    /**
     * Adds the components needed to the
     */
    private void addComponents() {
        this.getContentPane().add(addLabel);
        this.getContentPane().add(dataField);
        this.getContentPane().add(addButton);
        this.getContentPane().add(fiveNumberSummaryButton);
        this.getContentPane().add(generalInfoButton);
        this.getContentPane().add(straysAndOutlierButton);
        this.getContentPane().add(displayScrollPane);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();

        if (actionCommand.equals("add")) {
            addToDisplay();
        } else if (data.size() == 0) {
            JOptionPane.showMessageDialog(this, "No data entered yet!");
        } else if (actionCommand.equals("summary")) {
            fiveNumberSummary(data);
            showFiveNumberSummary();
        } else if (actionCommand.equals("general")) {
            generalInfo(data);
        } else if (actionCommand.equals("strayandoutlier")) {
            fiveNumberSummary(data);
            findStraysAndOutliers();
            showStraysAndOutliers();
        } else if (actionCommand.equals("undo")) {
            undoLast();
        } else if (actionCommand.equals("sort")) {
            sortDisplay();
        } else if (actionCommand.equals("clear")) {
            clearDisplay(true);
        } else if (actionCommand.equals("about")) {
            utility.aboutDialog();
        } else if (actionCommand.equals("help")) {
            utility.helpDialog();
        } else if (actionCommand.equals("open")) {
            openFromFile();
        } else if (actionCommand.equals("save")) {
            saveToFile();
        } else if (actionCommand.equals("quit")) {
            quit();
        }
    }

    /**
     * For adding to the display when not using the JTextField.
     *
     * @param d double containing the value to be added to the JTextArea.
     */
    private void addToDisplay(double d) {
        if (count == 0 || count % 6 == 0) {
            display.append("\n(" + (count + 1) + ") " + d);
        } else {
            display.append("\t(" + (count + 1) + ") " + d);
        }
        count++;
    }

    /**
     * Refreshes the display by reprinting the data.
     */
    private void refreshDisplay() {
        for (Double dataPoint : data) {
            addToDisplay(dataPoint);
        }
    }

    /**
     * Removes data from the ArrayList<Double> and/or from the JTextArea.
     *
     * @param delete boolean that determines whether or not the JTextArea will just
     *               be cleared (false) or if it will also erase data.
     */
    private void clearDisplay(boolean delete) {
        display.setText(displayHeading);
        if (delete) {
            data.clear();
        }
        count = 0;
        sum = 0;
        dataField.requestFocus();
    }

    /**
     * Calculates and returns the data needed for the five number summary.
     *
     * @param toCalc ArrayList<Double> that is going to be operated on.
     */
    private void fiveNumberSummary(List<Double> toCalc) {
        int n = toCalc.size() - 1;

        if (! toCalc.isEmpty() && n != 0) {
            Double[] numbers = toCalc.toArray(new Double[toCalc.size()]);
            Arrays.sort(numbers);

            // Max and Min
            max = numbers[n];
            min = numbers[0];

            // Middle Number (Q2)
            median = getMiddleNum(numbers);

            // Q1
            firstQuartile = getMiddleNum(Arrays.copyOfRange(numbers, 0, (numbers.length) / 2));

            // Q3
            if (numbers.length % 2 == 0) {
                thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers, (numbers.length) /
                                                                         2, numbers.length));
            } else {
                thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers, ((numbers.length) / 2) +
                                                                         1, numbers.length));
            }
        } else {
            max = min = median = firstQuartile = thirdQuartile = toCalc.get(0);
        }

        upperOutlier = median + (6 * (thirdQuartile - median));
        lowerOutlier = median - (6 * (median - firstQuartile));

        upperStray = median + (3 * (thirdQuartile - median));
        lowerStray = median - (3 * (median - firstQuartile));

        dataField.requestFocus();
    }

    private void showFiveNumberSummary() {
        String toDisplay =
                "Minimum: %s\nFirst Quartile: %.2f\nMedian: %.2f\nThird Quartile: %.2f\nMaximum: " +
                "%s";
        JOptionPane.showMessageDialog(this, String.format(toDisplay, min, firstQuartile, median,
                                                          thirdQuartile, max),
                                      "Five Number Summary", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Finds all the strays and outliers in the dataset.
     */
    private void findStraysAndOutliers() {
        for (Double dataPoint : data) {
            if (dataPoint > upperOutlier || dataPoint < lowerOutlier) {
                outlierList.add(dataPoint);
            } else if (dataPoint > upperStray || dataPoint < lowerStray) {
                strayList.add(dataPoint);
            }
        }
    }


    private void showStraysAndOutliers() {
        String strays = strayList.toString().replace("[", "").replace("]", "");
        if (strays.equals("")) {
            strays = "None";
        }

        String outliers = outlierList.toString().replace("[", "").replace("]", "");
        if (outliers.equals("")) {
            outliers = "None";
        }
        String strayAndOutlierText = "Strays: " + strays +
                                     "\nUpper Stray Bound: " + upperStray +
                                     "\nLower Stray Bound: " + lowerStray +
                                     "\n\nOutliers: " + outliers +
                                     "\nUpper Outlier Bound: " + upperOutlier +
                                     "\nLower Outlier Bound: " + lowerOutlier;
        JOptionPane.showMessageDialog(this, strayAndOutlierText);
    }

    /**
     * Finds and returns the middle index of a 1D Double array.
     *
     * @param num Double [] is the array that is going to have its middle index
     *            returned.
     * @return double index from the middle of the Double array.
     */
    private double getMiddleNum(Double[] num) {
        int middle = (num.length - 1) / 2;

        if (num.length % 2 == 0) {
            double num1 = num[middle];
            double num2 = num[middle + 1];
            return (num1 + num2) / 2;
        } else {
            return num[middle];
        }
    }

    @Override
    public void keyTyped(KeyEvent kt) {
        if (kt.getKeyChar() == '\n') {
            addToDisplay();
        }
    }

    /**
     * Adds the user input to the JTextArea. Formats the layout using \t.
     */
    private void addToDisplay() {
        if (! dataField.getText().isEmpty()) {
            try {
                double newData = Double.parseDouble(dataField.getText());
                sum += newData;
                dataField.setText("");
                data.add(newData);
                if (count == 0 || count % 6 == 0) {
                    display.append("\n(" + (count + 1) + ") " + newData);
                } else {
                    display.append("\t(" + (count + 1) + ") " + newData);
                }
                recentlySaved = false;
                count++;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "The input is not valid!", "Input Error",
                                              JOptionPane.ERROR_MESSAGE);
                dataField.setText("");
            }
        }
        dataField.requestFocus();
    }

    @Override
    public void keyPressed(KeyEvent kp) {

    }

    @Override
    public void keyReleased(KeyEvent kr) {

    }

    /**
     * Displays the other information about the dataset.
     *
     * @param otherCalc List<Double> that is to be used to get the answers.
     */
    private void generalInfo(List<Double> otherCalc) {
        double n = 0;
        double mean = 0;
        if (! otherCalc.isEmpty()) {
            Collections.sort(otherCalc);

            // Average
            n = otherCalc.size();
            mean = sum / n;
        }

        String otherInfoString =
                "Total Data Entries: %s\nSum: %.2f\nMean: %.2f\nStandard Deviation: %.3f";
        JOptionPane.showMessageDialog(this, String.format(otherInfoString, n, sum, mean,
                                                          calcStandardDeviation(mean)),
                                      "Other Info", JOptionPane.PLAIN_MESSAGE);
        dataField.requestFocus();
    }

    private double calcStandardDeviation(double mean) {
        double total = 0;

        for (double i : data) {
            total += Math.pow((i - mean), 2);
        }

        return Math.sqrt((Math.pow(data.size(), - 1) * total));
    }

    /**
     * Gets input from a txt file with the values each on a new line(The format
     * that the save method uses).
     */
    private void openFromFile() {
        boolean cancelFlag = false;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filterTxt = new FileNameExtensionFilter("Text Files", "txt");

        chooser.setFileFilter(filterTxt);
        boolean suitableToRead = false;
        mainLoop:
        while (true) {
            int returnVal = chooser.showOpenDialog(getParent());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File newFile = chooser.getSelectedFile();

                if (! newFile.exists()) {
                    JOptionPane.showMessageDialog(this, "The file you requested does not exist.",
                                                  "File not found", JOptionPane.WARNING_MESSAGE);
                } else {
                    List<Double> fileInput = new ArrayList<Double>();
                    try {
                        Scanner scanf = new Scanner(newFile);

                        while (scanf.hasNextLine()) {
                            if (!suitableToRead) {
                                String check = scanf.nextLine();
                                if (check.equals("FNS")) {
                                    suitableToRead = true;
                                } else {
                                    break mainLoop;
                                }
                            }
                            fileInput.add(Double.parseDouble(scanf.nextLine()));
                        }
                        scanf.close();
                        data = new ArrayList<Double>(fileInput);
                        fileInput.clear();
                        refreshDisplay();
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "The file could not be read.",
                                                      "File not accessible",
                                                      JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                }
            } else {
                cancelFlag = true;
                break;
            }

        }
        if (! suitableToRead && ! cancelFlag) {
            JOptionPane.showMessageDialog(this, "The file could not be read as it isn't suitable" +
                                                ".\nAdd FNS as the first line of the text file.",
                                          "File not suitable", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Sorts the ArrayList<Double> in ascending order and then outputs it into
     * the JTextArea.
     */
    private void sortDisplay() {
        if (! data.isEmpty()) {
            Collections.sort(data);

            clearDisplay(false);
            refreshDisplay();
        }
        dataField.requestFocus();
    }

    /**
     * Removes the previous data entry. (WIP) Probably going to use another list
     * to track changes.
     */
    private void undoLast() {
        if (! data.isEmpty()) {
            sum -= data.remove(data.size() - 1);

            clearDisplay(false);
            refreshDisplay();
        }
    }

    @Override
    public void windowOpened(WindowEvent arg0) {

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        quit();
    }

    /**
     * Handles quitting. If the data ArrayList<Double> has any elements it
     * prompts the user to save the data.
     */
    private void quit() {
        if ((data.size() > 0) && ! recentlySaved) {
            int quitChoice =
                    JOptionPane.showConfirmDialog(this, "Do you want to save before you quit?");
            if (quitChoice == 0) {
                saveToFile();
            }
        }
        System.exit(0);
    }

    /**
     * Handles the user input part of saving.
     */
    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(getParent());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File newFile = chooser.getSelectedFile();

            if (! newFile.exists()) {
                createFile(newFile);
            } else {
                int choice = JOptionPane.showConfirmDialog(this, "The file " + newFile.getName() +
                                                                 " wasn't able to be created " +
                                                                 "because it already exists.\n" +
                                                                 "Would you like to overwrite the" +
                                                                 " file?", "File Already Exists",
                                                           JOptionPane.YES_NO_OPTION);
                if (choice == 0) {
                    createFile(newFile);
                }

            }

        }

    }

    /**
     * Handles the creation of the new file and the writing of existing data to
     * it.
     *
     * @param newFile File that must be created and have data written to.
     */
    private void createFile(File newFile) {
        File fileOut;
        if (newFile.getName().endsWith(".txt")) {
            fileOut = new File(newFile.getPath());
        } else {
            fileOut = new File(newFile.getPath() + ".txt");
        }

        try {
            FileWriter fw = new FileWriter(fileOut);
            fw.append("FNS\r\n");

            for (Double i : data) {
                fw.append(String.valueOf(i)).append("\r\n");
            }
            fw.flush();
            fw.close();
            recentlySaved = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void windowClosed(WindowEvent arg0) {

    }

    @Override
    public void windowIconified(WindowEvent arg0) {

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {

    }

    @Override
    public void windowActivated(WindowEvent arg0) {

    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {

    }
}
