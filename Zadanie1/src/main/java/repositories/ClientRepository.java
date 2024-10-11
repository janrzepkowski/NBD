package repositories;

import models.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository implements Repository<Client> {
    private List<Client> clients = new ArrayList<>();

    @Override
    public Optional<Client> get(UUID clientId) {
        return clients.stream().filter(client -> client.getClientId().equals(clientId)).findFirst();
    }

    @Override
    public List<Client> getAll() {
        return new ArrayList<>(clients);
    }

    @Override
    public Client add(Client client) {
        clients.add(client);
        return client;
    }

    @Override
    public boolean remove(Client client) {
        return clients.remove(client);
    }

    @Override
    public Client update(Client client) {
        return null;
    }
}