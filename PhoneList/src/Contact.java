import java.util.List;

public class Contact {
    private String firstName;
    private String lastName;
    private List<String> phoneNumbers;

    public Contact(String firstName, String lastName, List<String> phoneNumbers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumbers = phoneNumbers;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " - " + String.join(", ", phoneNumbers);
    }
}
