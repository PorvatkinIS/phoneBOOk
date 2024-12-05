import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhoneBook {
    private final List<Contact> contacts;
    private final DataBaseConnection dbConnection;

    public PhoneBook() {
        this.contacts = new ArrayList<>();
        this.dbConnection = new DataBaseConnection();
        loadContactsFromDatabase();
    }

    private void loadContactsFromDatabase() {
        contacts.addAll(dbConnection.getAllContacts());
    }

    public void addContact(String firstName, String lastName, List<String> phoneNumbers) {
        if (isUniqueContact(firstName, lastName)) {
            Contact newContact = new Contact(firstName, lastName, phoneNumbers);
            contacts.add(newContact);
            dbConnection.addContact(newContact);
            sortContacts();
        } else {
            JOptionPane.showMessageDialog(null, "Контакт с таким именем и фамилией уже существует.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeContact(String firstName, String lastName) {
        Optional<Contact> contactToRemove = contacts.stream()
                .filter(contact -> contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (contactToRemove.isPresent()) {
            int response = JOptionPane.showConfirmDialog(null,
                    "Вы уверены, что хотите удалить контакт " + contactToRemove.get() + "?",
                    "Подтверждение удаления",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                contacts.remove(contactToRemove.get());
                dbConnection.removeContact(firstName, lastName);
                JOptionPane.showMessageDialog(null, "Контакт удален.", "Информация", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Контакт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editContact(String firstName, String lastName, List<String> newPhoneNumbers) {
        Optional<Contact> contactOpt = contacts.stream()
                .filter(contact -> contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        contactOpt.ifPresent(contact -> {
            contact.setPhoneNumbers(newPhoneNumbers);
            dbConnection.editContact(contact);
        });

        if (contactOpt.isPresent()) {
            sortContacts();
        } else {
            JOptionPane.showMessageDialog(null, "Контакт не найден.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isUniqueContact(String firstName, String lastName) {
        return contacts.stream().noneMatch(contact -> contact.getFirstName().equalsIgnoreCase(firstName) && contact.getLastName().equalsIgnoreCase(lastName));
    }

    private void sortContacts() {
        contacts.sort((c1, c2) -> {
            int lastNameComparison = c1.getLastName().compareToIgnoreCase(c2.getLastName());
            return lastNameComparison != 0 ? lastNameComparison : c1.getFirstName().compareToIgnoreCase(c2.getFirstName());
        });
    }

    public void close() {
        dbConnection.close();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<Contact> searchByLastName(String lastName) {
        return List.of();
    }

    public void displayContacts(JTextArea outputArea) {
    }
}
