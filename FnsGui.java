/**
 * @author Jethro Muller
 * @version 0.1 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class FnsGui extends JFrame implements ActionListener, KeyListener {
	private final Dimension windowDimensions = new Dimension(700, 500);
	private final Font displayFont = new Font("Arial", Font.BOLD, 12);
	private final String toDisplay = "Minimum: %s\nFirst Quartile: %.2f\nMedian: %.2f\nThird Quartile: %.2f\nMaximum: %s";// 9
	private final String otherInfoString = "Total Data Entries: %s\nSum: %.2f\nMean: %.2f";// 3
	private final String displayHeading = "  Data:\n=======================";
	private int count = 0;

	private JLabel addLabel = null;
	private JTextField dataField = null;
	private JButton addButton = null;
	private JButton fiveNumberSummaryButton = null;
	private JButton otherInfoButton = null;
	private JTextArea display = null;
	JScrollPane displayScrollPane = null;

	private JButton sortButton = null;
	private JButton undoAddButton = null;
	private JButton clearButton = null;
	private JButton aboutButton = null;

	private List<Double> data = null;

	/*
	 * Laziness
	 * 
	 * private static void logln(Object o) { System.out.println(o); }
	 * 
	 * private static void log(Object o) { System.out.print(o); }
	 */

	public FnsGui() {
		createJFrame();

		data = new ArrayList<Double>();

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
		fiveNumberSummaryButton.setPreferredSize(new Dimension(175, 25));
		fiveNumberSummaryButton.addActionListener(this);

		otherInfoButton = new JButton("Other Info");
		otherInfoButton.setActionCommand("other");
		otherInfoButton.setPreferredSize(new Dimension(150, 25));
		otherInfoButton.addActionListener(this);

		display = new JTextArea(displayHeading);
		display.setBorder(new LineBorder(Color.BLACK, 2, true));
		display.setFont(displayFont);

		displayScrollPane = new JScrollPane(display);
		displayScrollPane.setPreferredSize(new Dimension(getX(), 350));

		sortButton = new JButton("Sort Data");
		sortButton.setActionCommand("sort");
		sortButton.setPreferredSize(new Dimension(150, 25));
		sortButton.addActionListener(this);

		undoAddButton = new JButton("Undo Add");
		undoAddButton.setActionCommand("undo");
		undoAddButton.setPreferredSize(new Dimension(150, 25));
		undoAddButton.addActionListener(this);

		clearButton = new JButton("Clear Data");
		clearButton.setActionCommand("clear");
		clearButton.setPreferredSize(new Dimension(150, 25));
		clearButton.addActionListener(this);

		aboutButton = new JButton("Help/About");
		aboutButton.setActionCommand("about");
		aboutButton.setPreferredSize(new Dimension(150, 25));
		aboutButton.addActionListener(this);

		this.getContentPane().add(addLabel);
		this.getContentPane().add(dataField);
		this.getContentPane().add(addButton);
		this.getContentPane().add(fiveNumberSummaryButton);
		this.getContentPane().add(otherInfoButton);
		this.getContentPane().add(displayScrollPane);
		this.getContentPane().add(undoAddButton);
		this.getContentPane().add(sortButton);
		this.getContentPane().add(clearButton);
		this.getContentPane().add(aboutButton);

	}

	private void aboutHelpDialog() {
		JOptionPane
				.showMessageDialog(
						this,
						"Welcome to Five Number Summary Calculator\n\n"
								+ "To add numbers click in the textbox, type in a number and click Add or push Enter/Return.\n"
								+ "To undo your last Add click Undo Add.\n"
								+ "To view the Five Number Summary, click the Five Number Summary button.\n"
								+ "To view the other information click the Other Information button.\n"
								+ "To rearrange the data on screen, click Sort Data.\n"
								+ "To clear the data, click the Clear Data button.\n\n"
								+ "About\n==========================\nThis application calculates the values for the five number summary, it also calculates the Sum and Average.\n\n"
								+ "This was made in a few hours for fun on a public holiday.\n"
								+ "Five Number Summary Calculator v1.0\n"
								+ "Made by Jethro Muller\n" + "September 2013",
						"Help/About", JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();

		switch (actionCommand) {
		case "add":
			addToDisplay();
			break;
		case "summary":
			fiveNumberSummary(data);
			break;
		case "other":
			otherInfo(data);
			break;
		case "undo":
			undoLast();
			break;
		case "sort":
			sortDisplay();
			break;
		case "clear":
			clearDisplay(true);
			break;
		case "about":
			aboutHelpDialog();
			break;
		}
	}

	private void addToDisplay() {
		if (!dataField.getText().isEmpty()) {
			try {
				double newData = Double.parseDouble(dataField.getText());
				dataField.setText("");
				data.add(newData);
				if (count == 0) {
					display.append("\n" + (count + 1) + ". " + newData);
				} else if (count % 6 != 0) {
					display.append("\t" + (count + 1) + ". " + newData);
				} else {
					display.append("\n" + (count + 1) + ". " + newData);
				}
				count++;
				dataField.requestFocus();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "The input is not valid!",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		dataField.requestFocus();
	}

	private void addToDisplay(double d) {
		double newData = d;
		if (count == 0) {
			display.append("\n" + (count + 1) + ". " + newData);
		} else if (count % 6 != 0) {
			display.append("\t" + (count + 1) + ". " + newData);
		} else {
			display.append("\n" + (count + 1) + ". " + newData);
		}
		count++;
		dataField.requestFocus();
	}

	private void clearDisplay(boolean delete) {
		display.setText(displayHeading);
		if (delete) {
			data = new ArrayList<Double>();
		}
		count = 0;
		dataField.requestFocus();
	}

	/**
	 * Initializes the main JFrame and changes all necessary settings.
	 */
	private void createJFrame() {
		setTitle("Five Number Summary Calculator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(windowDimensions);
		pack();
		setLayout(new FlowLayout());
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private void fiveNumberSummary(List<Double> toCalc) {
		int n = toCalc.size() - 1;
		double median = 0;
		double firstQuartile = 0;
		double thirdQuartile = 0;
		double max = 0;
		double min = 0;

		if (!toCalc.isEmpty()) {
			Double[] numbers = toCalc.toArray(new Double[0]);
			Arrays.sort(numbers);

			// Max and Min
			max = numbers[n];
			min = numbers[0];

			// Middle Number (Q2)
			median = getMiddleNum(numbers);

			// Q1
			firstQuartile = getMiddleNum(Arrays.copyOfRange(numbers, 0,
					(numbers.length) / 2));

			// Q3
			if (numbers.length % 2 == 0) {
				thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers,
						(numbers.length) / 2, numbers.length));
			} else {
				thirdQuartile = getMiddleNum(Arrays.copyOfRange(numbers,
						((numbers.length) / 2) + 1, numbers.length));
			}
		}
		JOptionPane.showMessageDialog(this, String.format(toDisplay, min,
				firstQuartile, median, thirdQuartile, max),
				"Five Number Summary", JOptionPane.PLAIN_MESSAGE);
		dataField.requestFocus();
	}

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
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent kt) {
		if (kt.getKeyChar() == '\n') {
			addToDisplay();
		}
	}

	private void otherInfo(List<Double> otherCalc) {
		double n = 0;
		double total = 0;
		double mean = 0;
		if (!otherCalc.isEmpty()) {
			Double[] numbers = otherCalc.toArray(new Double[0]);
			Arrays.sort(numbers);

			// Total
			total = 0;
			for (Double i : numbers)
				total += i;

			// Average
			n = otherCalc.size();
			mean = total / n;
		}

		JOptionPane.showMessageDialog(this,
				String.format(otherInfoString, n, total, mean), "Other Info",
				JOptionPane.PLAIN_MESSAGE);
		dataField.requestFocus();
	}

	private void sortDisplay() {
		if (!data.isEmpty()) {
			Double[] numbers = data.toArray(new Double[0]);
			Arrays.sort(numbers);
			clearDisplay(false);

			for (Double i : numbers)
				addToDisplay(i);
		}
	}

	private void undoLast() {
		if (!data.isEmpty()) {
			Double[] numbers = data.toArray(new Double[0]);
			numbers = Arrays.copyOfRange(numbers, 0, numbers.length - 1);
			data = Arrays.asList(numbers);
			clearDisplay(false);

			for (Double i : numbers)
				addToDisplay(i);
		}
	}
}