package managers;

import models.Client;

import java.util.List;
import java.util.Optional;

public class ClientManager {
    private List<Client> clients;

    public ClientManager(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Optional<Client> getClient(String personalId) {
        return clients.stream().filter(client -> client.getPersonalId().equals(personalId)).findFirst();
    }

    public void registerClient(String personalId, String firstName, String lastName, String phoneNumber) {
        if (clients.stream().anyMatch(client -> client.getPersonalId().equals(personalId))) {
            throw new IllegalArgumentException("Client with personalId " + personalId + " already exists.");
        }
        clients.add(new Client(personalId, firstName, lastName, phoneNumber));
    }

    public void unregisterClient(String personalId) {
        if (!clients.removeIf(client -> client.getPersonalId().equals(personalId))) {
            throw new IllegalArgumentException("Client with personalId " + personalId + " does not exist.");
        }
        clients.removeIf(client -> client.getPersonalId().equals(personalId));
    }

    public String getClientsInfo() {
        StringBuilder clientsInfo = new StringBuilder();
        clients.forEach(client -> clientsInfo.append(client.getClientInfo()).append("\n\n"));
        return clientsInfo.toString();
    }
}