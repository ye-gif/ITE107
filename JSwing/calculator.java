import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private String num1 = "", num2 = "", operator = "";

    public Calculator() {
        // Set up the JFrame
        setTitle("Calculator");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display panel
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 20));
        add(display, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4));

        // Buttons
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem historyItem = new JMenuItem("View History");
        JMenuItem exitItem = new JMenuItem("Exit");

        historyItem.addActionListener(this);
        exitItem.addActionListener(this);

        menu.add(historyItem);
        menu.add(exitItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("0123456789".contains(command)) {
            display.setText(display.getText() + command);
        } else if ("/*-+".contains(command)) {
            num1 = display.getText();
            operator = command;
            display.setText("");
        } else if ("=".equals(command)) {
            num2 = display.getText();
            try {
                double result = calculate(Double.parseDouble(num1), Double.parseDouble(num2), operator);
                display.setText(String.valueOf(result));
                saveToHistory(num1, operator, num2, result);
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else if ("C".equals(command)) {
            display.setText("");
            num1 = num2 = operator = "";
        } else if ("View History".equals(command)) {
            viewHistory();
        } else if ("Exit".equals(command)) {
            System.exit(0);
        }
    }

    private double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) throw new ArithmeticException("Cannot divide by zero");
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private void saveToHistory(String num1, String operator, String num2, double result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("calculator_history.txt", true))) {
            writer.write(num1 + " " + operator + " " + num2 + " = " + result + "\n");
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    private void viewHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("calculator_history.txt"))) {
            StringBuilder history = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(this, history.toString(), "Calculation History", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No history found.", "Calculation History", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
