package models;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(defaultKeyspace = "client")
@CqlName("clients")
public class Client implements Serializable {

    @PartitionKey
    @CqlName("clientId")
    private UUID clientId;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int rents;
    private boolean archived;

    public Client(UUID uuid, String firstName, String lastName, String phoneNumber) {
        this.clientId = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.rents = 0;
        this.archived = false;
    }

    public Client() {
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public int getRents() {
        return rents;
    }

    public void setRents(int rents) {
        this.rents = rents;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", rents=" + rents +
                ", archived=" + archived +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return archived == client.archived && Objects.equals(clientId, client.clientId) && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName) && Objects.equals(phoneNumber, client.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName, phoneNumber, archived);
    }
}
