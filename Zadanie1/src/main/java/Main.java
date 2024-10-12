import managers.ClientManager;
import repositories.ClientRepository;
import models.Client;

public class Main {

    public static void main(String[] args) {
        Client client1 = new Client("John", "Doe", "555-1234");
        Client client2 = new Client("Laura", "Palmer", "555-9876");
        ClientRepository clientRepository = new ClientRepository();
        ClientManager clientManager = new ClientManager(clientRepository);

        // Rejestracja
        clientManager.registerClient(client1);
        clientManager.registerClient(client2);

        // Pobieramy wszystkich klientów
        System.out.println("\nAll registered clients:");
        for (Client client : clientRepository.getAll()) {
            System.out.println(client.getClientInfo());
        }

        // Aktualizujemy dane klienta
        client1.setPhoneNumber("213-8678");
        clientRepository.update(client1);

        // Pobieramy zaktualizowanego klienta
        Client updatedClient = clientRepository.get(client1.getClientId());
        System.out.println("\nUpdated client info:");
        System.out.println(updatedClient.getClientInfo());

        // Usuwamy klienta
        System.out.println("\nRemoving client...");
        clientRepository.remove(client2);

        System.out.println("\nRemaining clients after removal:");
        for (Client client : clientRepository.getAll()) {
            System.out.println(client.getClientInfo());
        }
    }
}
