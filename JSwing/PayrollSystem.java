import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PayrollSystem extends JFrame {

    // components for input fields
    private JTextField txtEmployeeId, txtName, txtHourlyRate, txtHoursWorked;
    private JTextArea txtOutput;

    // file to store employee records
    private final String FILE_NAME = "payroll.txt";

    public PayrollSystem() {
        // frame configuration
        setTitle("Payroll System");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(152, 122, 172)); // background rgb color purple

        // title label
        JLabel lblTitle = new JLabel("Payroll System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(300, 20, 400, 40);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle);

        // labels and text fields
        addLabel("Employee ID:", 50, 100);
        txtEmployeeId = addTextField(200, 100);

        addLabel("Name:", 50, 140);
        txtName = addTextField(200, 140);

        addLabel("Hourly Rate:", 50, 180);
        txtHourlyRate = addTextField(200, 180);

        addLabel("Hours Worked:", 50, 220);
        txtHoursWorked = addTextField(200, 220);

        // buttons
        JButton btnAddEmployee = addButton("Add Employee", 50, 280);
        JButton btnSaveRecord = addButton("Save Record", 200, 280);
        JButton btnDisplayRecord = addButton("Display Record", 50, 330);
        JButton btnViewSalary = addButton("View Salary", 200, 330);

        // text area for output
        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtOutput.setBackground(new Color(235, 208, 235)); // rgb color light pink
        JScrollPane scrollPane = new JScrollPane(txtOutput);
        scrollPane.setBounds(400, 100, 450, 300);
        add(scrollPane);

        // button actions
        btnAddEmployee.addActionListener(e -> addEmployee());
        btnSaveRecord.addActionListener(e -> saveRecords());
        btnDisplayRecord.addActionListener(e -> displayRecords());
        btnViewSalary.addActionListener(e -> viewSalary());

        // to load existing records on startup
        displayRecords();

        // set the frame visible
        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 120, 30);
        add(label);
        return label;
    }

    private JTextField addTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 150, 30);
        add(textField);
        return textField;
    }

    private JButton addButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBounds(x, y, 130, 30);
        button.setBackground(new Color(119, 221, 191)); // light green
        button.setFocusPainted(false);
        add(button);
        return button;
    }

    // for adding employee to the output area
    private void addEmployee() {
        String employeeId = txtEmployeeId.getText();
        String name = txtName.getText();
        String hourlyRate = txtHourlyRate.getText();
        String hoursWorked = txtHoursWorked.getText();

        if (employeeId.isEmpty() || name.isEmpty() || hourlyRate.isEmpty() || hoursWorked.isEmpty()) {
            txtOutput.setText("Please fill in all fields.");
            return;
        }

        try {
            double rate = Double.parseDouble(hourlyRate);
            double hours = Double.parseDouble(hoursWorked);
            double grossPay = rate * hours;

            txtOutput.setText("Employee Added:\n" +
                    "ID: " + employeeId + "\n" +
                    "Name: " + name + "\n" +
                    "Hourly Rate: " + hourlyRate + "\n" +
                    "Hours Worked: " + hoursWorked + "\n" +
                    "Gross Pay: $" + grossPay);

        } catch (NumberFormatException ex) {
            txtOutput.setText("Invalid input for rate or hours. Please enter valid numbers.");
        }
    }

    // for saving employee records to file
    private void saveRecords() {
        String employeeId = txtEmployeeId.getText();
        String name = txtName.getText();
        String hourlyRate = txtHourlyRate.getText();
        String hoursWorked = txtHoursWorked.getText();

        if (employeeId.isEmpty() || name.isEmpty() || hourlyRate.isEmpty() || hoursWorked.isEmpty()) {
            txtOutput.setText("Please fill in all fields.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(employeeId + "," + name + "," + hourlyRate + "," + hoursWorked);
            writer.newLine();
            txtOutput.setText("Record saved successfully.");
            clearInputFields();
        } catch (IOException ex) {
            txtOutput.setText("Error saving the record.");
        }
    }

    // to display all employee records
    private void displayRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            txtOutput.setText("Employee Records:\n");
            String line;
            while ((line = reader.readLine()) != null) {
                txtOutput.append(line + "\n");
            }
        } catch (IOException ex) {
            txtOutput.setText("No records found or error reading the file.");
        }
    }

    // to view employee salary
    private void viewSalary() {
        String employeeID = txtEmployeeId.getText();  
        
        if (employeeID.isEmpty()) {
            txtOutput.setText("Please enter an Employee ID.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("payroll.txt"))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].equals(employeeID)) {
                    // for employee found, retrieve details
                    String name = fields[1];
                    double hourlyRate = Double.parseDouble(fields[2]);
                    double hoursWorked = Double.parseDouble(fields[3]);

                    // to calculate gross pay, tax, and net pay
                    double grossPay = hourlyRate * hoursWorked;
                    double tax = grossPay * 0.2; // 20% tax deduction
                    double netPay = grossPay - tax;

                    // to display salary details
                    txtOutput.setText(
                        "Employee Salary Details:\n" +
                        "--------------------------\n" +
                        "Employee ID: " + employeeID + "\n" +
                        "Name: " + name + "\n" +
                        "Hourly Rate: $" + hourlyRate + "\n" +
                        "Hours Worked: " + hoursWorked + "\n" +
                        "Gross Pay: $" + String.format("%.2f", grossPay) + "\n" +
                        "Tax Deduction (20%): $" + String.format("%.2f", tax) + "\n" +
                        "Net Pay: $" + String.format("%.2f", netPay)
                    );
                    found = true;
                    break;
                }
            }

            if (!found) {
                txtOutput.setText("Employee with ID " + employeeID + " not found.");
            }
        } catch (IOException e) {
            txtOutput.setText("Error reading payroll file: " + e.getMessage());
        } catch (NumberFormatException e) {
            txtOutput.setText("Error in employee data format: " + e.getMessage());
        }
    }

    private void clearInputFields() {
        txtEmployeeId.setText("");
        txtName.setText("");
        txtHourlyRate.setText("");
        txtHoursWorked.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollSystem::new);
    }
}
