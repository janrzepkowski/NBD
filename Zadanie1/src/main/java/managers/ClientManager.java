package managers;

import models.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientManager {
    private List<Client> clients;

    public ClientManager(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Optional<Client> getClient(UUID clientId) {
        return clients.stream().filter(client -> client.getClientId().equals(clientId)).findFirst();
    }

    public void registerClient(String firstName, String lastName, String phoneNumber) {
        clients.add(new Client(firstName, lastName, phoneNumber));
    }

    public void unregisterClient(UUID clientId) {
        clients.removeIf(client -> client.getClientId().equals(clientId));
    }

    public String getClientsInfo() {
        StringBuilder clientsInfo = new StringBuilder();
        clients.forEach(client -> clientsInfo.append(client.getClientInfo()).append("\n\n"));
        return clientsInfo.toString();
    }
}