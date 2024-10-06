package org.nbd.models;

public class Client {
    private final String personalId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean archived;

    public Client(String personalId, String firstName, String lastName, String phoneNumber) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.archived = false;
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

    public boolean isArchived() {
        return archived;
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

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getClientInfo() {
        return "ID: " + personalId + "\nFirst name: " + firstName + "\nLast name: " + lastName + "\nPhone number: " + phoneNumber + "\nArchived: " + archived;
    }
}
