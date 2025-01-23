import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Phonebook extends JFrame implements ActionListener {
    private JTextField nameField, phoneField, searchField;
    private JTextArea displayArea;
    private HashMap<String, String> contacts;

    public Phonebook() {
        // Initialize the phonebook
        contacts = new HashMap<>();

        // Set up the JFrame
        setTitle("Phonebook");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for adding contacts
        JPanel addPanel = new JPanel(new GridLayout(3, 2));
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Contact"));
        addPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        addPanel.add(nameField);
        addPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        addPanel.add(phoneField);
        JButton addButton = new JButton("Add Contact");
        addButton.addActionListener(this);
        addPanel.add(addButton);

        // Center panel for displaying contacts
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder("Contacts"));
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(scrollPane);

        // Bottom panel for actions
        JPanel actionPanel = new JPanel(new GridLayout(2, 1));

        // Search and Delete Panel
        JPanel searchDeletePanel = new JPanel(new GridLayout(1, 3));
        searchDeletePanel.setBorder(BorderFactory.createTitledBorder("Search/Delete Contact"));
        searchField = new JTextField();
        searchDeletePanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchDeletePanel.add(searchButton);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        searchDeletePanel.add(deleteButton);

        // Save and Load Panel
        JPanel filePanel = new JPanel(new GridLayout(1, 2));
        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(this);
        filePanel.add(saveButton);
        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(this);
        filePanel.add(loadButton);

        actionPanel.add(searchDeletePanel);
        actionPanel.add(filePanel);

        // Add panels to frame
        add(addPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Add Contact")) {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Phone fields cannot be empty.");
                return;
            }
            contacts.put(name, phone);
            displayContacts();
            nameField.setText("");
            phoneField.setText("");
        } else if (command.equals("Search")) {
            String name = searchField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a name to search.");
                return;
            }
            String phone = contacts.get(name);
            if (phone != null) {
                JOptionPane.showMessageDialog(this, name + ": " + phone);
            } else {
                JOptionPane.showMessageDialog(this, "Contact not found.");
            }
        } else if (command.equals("Delete")) {
            String name = searchField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a name to delete.");
                return;
            }
            if (contacts.remove(name) != null) {
                JOptionPane.showMessageDialog(this, "Contact deleted.");
            } else {
                JOptionPane.showMessageDialog(this, "Contact not found.");
            }
            displayContacts();
        } else if (command.equals("Save to File")) {
            saveContactsToFile();
        } else if (command.equals("Load from File")) {
            loadContactsFromFile();
            displayContacts();
        }
    }

    private void displayContacts() {
        displayArea.setText(""); // Clear the display area
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            displayArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
    }

    private void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("phonebook.txt"))) {
            for (Map.Entry<String, String> entry : contacts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Contacts saved to phonebook.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file: " + e.getMessage());
        }
    }

    private void loadContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("phonebook.txt"))) {
            contacts.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    contacts.put(parts[0], parts[1]);
                }
            }
            JOptionPane.showMessageDialog(this, "Contacts loaded from phonebook.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Phonebook();
    }
}
