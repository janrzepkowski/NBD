package managers;

import models.Client;
import repositories.ClientRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientManager implements Serializable {
    private ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(Client client) {
        Optional<Client> existingClient = clientRepository.get(client.getClientId());
        if (existingClient.isEmpty()) {
            return clientRepository.add(client);
        }
        return existingClient.get();
    }

    public void unregisterClient(Client client) {
        client.setArchived(true);
    }

}