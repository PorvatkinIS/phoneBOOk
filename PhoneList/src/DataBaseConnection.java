import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {
    private static final String DATABASE_URL = "jdbc:h2:~/ё"; // Путь к базе данных
    private Connection connection;

    public DataBaseConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, "sa", ""); // Параметры по умолчанию
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "first_name VARCHAR(255) NOT NULL," +
                "last_name VARCHAR(255) NOT NULL," +
                "phone_numbers VARCHAR(255) NOT NULL" +
                ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts (first_name, last_name, phone_numbers) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contact.getFirstName());
            pstmt.setString(2, contact.getLastName());
            pstmt.setString(3, String.join(",", contact.getPhoneNumbers()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT first_name, last_name, phone_numbers FROM contacts";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                List<String> phoneNumbers = List.of(rs.getString("phone_numbers").split(","));
                contacts.add(new Contact(firstName, lastName, phoneNumbers));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public void removeContact(String firstName, String lastName) {
        String sql = "DELETE FROM contacts WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editContact(Contact contact) {
        String sql = "UPDATE contacts SET phone_numbers = ? WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, String.join(",", contact.getPhoneNumbers()));
            pstmt.setString(2, contact.getFirstName());
            pstmt.setString(3, contact.getLastName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
