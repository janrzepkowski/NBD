package managers;

import models.Client;
import repositories.ClientRepository;

public class ClientManager {
    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        }
        this.clientRepository = clientRepository;
    }

    private boolean clientExists(long clientId) {
        return clientRepository.read(clientId) != null;
    }

    public Client getClient(long clientId) {
        return this.clientRepository.read(clientId);
    }

    public void registerClient(long clientId, String firstName, String lastName, String phoneNumber) {
        if (clientExists(clientId)) {
            throw new IllegalArgumentException("Client with the same ID already exists.");
        }
        Client newClient = new Client(clientId, firstName, lastName, phoneNumber);
        clientRepository.create(newClient);
    }

    public void deleteClient(long clientId) {
        if (clientExists(clientId)) {
            clientRepository.delete(clientId);
        }
    }

    public void updateClient(long clientId, String firstName, String lastName, String phoneNumber) {
        if (clientExists(clientId)) {
            Client client = new Client(clientId, firstName, lastName, phoneNumber);
            clientRepository.update(client);
        }
    }
}