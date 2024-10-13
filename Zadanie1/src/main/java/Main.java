import models.*;
import managers.*;
import repositories.*;


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
            System.out.println(client.getClientInfo() + "\n");
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

        // ------------------------- Pojazdy --------------------------------
        Car car1 = new Car("BOX 111G", "Toyota", 100, 'C', 1.8);
        Bicycle bicycle1 = new Bicycle("AMS 1", "Bianchi", 30);
        Moped moped1 = new Moped("NO 5", "Piaggio", 50, 0.5);
        VehicleRepository vehicleRepository = new VehicleRepository();
        VehicleManager vehicleManager = new VehicleManager(vehicleRepository);

        // Rejestracja
        vehicleManager.registerVehicle(car1);
        vehicleManager.registerVehicle(bicycle1);
        vehicleManager.registerVehicle(moped1);

        // Pobieramy wszystkie pojazdy
        System.out.println("\nAll registered vehicles:");
        for (Vehicle vehicle : vehicleRepository.getAll()) {
            System.out.println(vehicle.getVehicleInfo() + "\n");
        }

        // Aktualizujemy dane pojazdu
        car1.setPlateNumber("MAG 1C");
        vehicleRepository.update(car1);

        // Pobieramy zaktualizowany pojazd
        Vehicle updatedCar = vehicleRepository.get(car1.getVehicleId());
        System.out.println("\nUpdated vehicle info:");
        System.out.println(updatedCar.getVehicleInfo());

        // Usuwamy pojazd
        System.out.println("\nRemoving bicycle...");
        vehicleRepository.remove(bicycle1);

        // Pozostale pojazdy po usunieciu roweru
        System.out.println("\nRemaining vehicles after removal:");
        for (Vehicle vehicle : vehicleRepository.getAll()) {
            System.out.println(vehicle.getVehicleInfo() + "\n");
        }
    }
}
