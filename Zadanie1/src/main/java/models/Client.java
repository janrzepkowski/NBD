package models;

public class Client {
    private final String personalId;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Client(String personalId, String firstName, String lastName, String phoneNumber) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClientInfo() {
        return "ID: " + personalId + "\nFirst name: " + firstName + "\nLast name: " + lastName + "\nPhone number: " + phoneNumber + "\n";
    }
}
