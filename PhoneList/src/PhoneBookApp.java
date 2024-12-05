import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class PhoneBookApp extends JFrame implements ActionListener {
    private final PhoneBook phoneBook;

    // Компоненты GUI
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField phonesField;
    private final JTextArea outputArea;

    public PhoneBookApp() {
        phoneBook = new PhoneBook();

        // Настройка окна
        setTitle("Телефонная книга");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Панель для ввода данных
        JPanel panel = new JPanel();

        firstNameField = new JTextField(10);
        lastNameField = new JTextField(10);
        phonesField = new JTextField(20);

        JButton addButton = new JButton("Добавить контакт");
        JButton removeButton = new JButton("Удалить контакт");
        JButton editButton = new JButton("Редактировать контакт");
        JButton searchButton = new JButton("Поиск по фамилии");
        JButton showButton = new JButton("Показать все контакты");

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        searchButton.addActionListener(this);
        showButton.addActionListener(this);

        panel.add(new JLabel("Имя:"));
        panel.add(firstNameField);

        panel.add(new JLabel("Фамилия:"));
        panel.add(lastNameField);

        panel.add(new JLabel("Телефоны (через запятую):"));
        panel.add(phonesField);

        panel.add(addButton);
        panel.add(removeButton);
        panel.add(editButton);

        panel.add(searchButton);

        panel.add(showButton);

        add(panel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Добавить контакт":
                addContact();
                break;
            case "Удалить контакт":
                removeContact();
                break;
            case "Редактировать контакт":
                editContact();
                break;
            case "Поиск по фамилии":
                searchByLastName();
                break;
            case "Показать все контакты":
                showContacts();
                break;
        }
    }

    private void addContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        List<String> phonesToAdd = Arrays.asList(phonesField.getText().split(","));

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Имя и фамилия не могут быть пустыми.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phonesToAdd.stream().allMatch(this::isValidPhoneNumber)) {
            phoneBook.addContact(firstName, lastName, phonesToAdd);
            outputArea.append("Контакт добавлен: " + firstName + " " + lastName + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Некорректные номера телефонов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        phoneBook.removeContact(firstName, lastName);
        outputArea.append("Контакт удален: " + firstName + " " + lastName + "\n");
    }

    private void editContact() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        List<String> newPhones = Arrays.asList(phonesField.getText().split(","));

        if (newPhones.stream().allMatch(this::isValidPhoneNumber)) {
            phoneBook.editContact(firstName, lastName, newPhones);
            outputArea.append("Контакт отредактирован: " + firstName + " " + lastName + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Некорректные номера телефонов.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchByLastName() {
        String lastName = lastNameField.getText().trim();
        List<Contact> foundContacts = phoneBook.searchByLastName(lastName);

        if (foundContacts.isEmpty()) {
            outputArea.append("Контакты не найдены.\n");
        } else {
            foundContacts.forEach(contact -> outputArea.append(contact.toString() + "\n"));
        }
    }

    private void showContacts() {
        phoneBook.displayContacts(outputArea);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+?\\d{1,15}"); // Пример простой проверки
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PhoneBookApp());
    }
}
