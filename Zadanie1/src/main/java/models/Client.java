package models;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "Client")
@Access(AccessType.FIELD)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clientId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "archived")
    private boolean archived;

    public Client() {
    }

    public Client(String firstName, String lastName, String phoneNumber) {
        this.clientId = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.archived = false;
    }

    public UUID getClientId() {
        return clientId;
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
        this.archived = true;
    }

    public String getClientInfo() {
        return "ID: " + clientId + "\nFirst name: " + firstName + "\nLast name: " + lastName + "\nPhone number: " + phoneNumber + "\n" + (archived ? "Archived" : "Active") + "\n";
    }
}