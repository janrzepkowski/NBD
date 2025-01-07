package managers;

import models.Client;
import repositories.ClientRepository;

import java.io.Serializable;

public class ClientManager {

    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        }
        this.clientRepository = clientRepository;
    }

    private boolean clientExists(long clientId) {
        return clientRepository.read(clientId) != null;
    }

    public void registerClient(Client client) {
        if (clientExists(client.getClientId())) {
            throw new IllegalArgumentException("Client with the same ID already exists.");
        }
        clientRepository.create(client);
    }

    public void archiveClient(long clientId) {
        Client client = clientRepository.read(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client does not exist.");
        }
        client.setArchived(true);
        clientRepository.update(client);
    }

    public Client getClient(long clientId) {
        return clientRepository.read(clientId);
    }

    public void updateClient(Client client) {
        if (!clientExists(client.getClientId())) {
            throw new IllegalArgumentException("Cannot update a non-existing client.");
        }
        clientRepository.update(client);
    }
}
