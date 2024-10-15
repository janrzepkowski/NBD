package managers;

import models.Client;
import repositories.ClientRepository;

import java.io.Serializable;

public class ClientManager implements Serializable {

    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        if (clientRepository == null) {
            throw new IllegalArgumentException("clientRepository cannot be null");
        } else {
            this.clientRepository = clientRepository;
        }
    }

    public Client registerClient(Client client) {
        if (clientRepository.get(client.getClientId()) != null) {
            throw new IllegalArgumentException("Client with the same ID already exists.");
        }
        return clientRepository.add(client);
    }

    public void unregisterClient(Client client) {
        if (client != null) {
            client.setArchived(true);
            clientRepository.update(client);
        }
    }
}