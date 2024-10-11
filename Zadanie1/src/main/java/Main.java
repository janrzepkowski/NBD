import managers.ClientManager;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Test Vehicle class
        Vehicle vehicle = new Vehicle("ABC123", "Toyota", 100);
        System.out.println("Vehicle Info:");
        System.out.println(vehicle.getVehicleInfo());
        System.out.println();

        // Test MotorVehicle class
        MotorVehicle motorVehicle = new MotorVehicle("XYZ789", "Honda", 200, 1.8);
        System.out.println("MotorVehicle Info:");
        System.out.println(motorVehicle.getVehicleInfo());
        System.out.println();

        // Test Car class
        Car car = new Car("LMN456", "BMW", 300, 'C', 2.5);
        System.out.println("Car Info:");
        System.out.println(car.getVehicleInfo());
        System.out.println();

        //Test Bicycle class
        Bicycle bicycle = new Bicycle("QWE321", "Polygon", 50);
        System.out.println("Bicycle Info:");
        System.out.println(bicycle.getVehicleInfo());
        System.out.println();

        //Test Moped class
        Moped moped = new Moped("ASD654", "Vespa", 150, 0.5);
        System.out.println("Moped Info:");
        System.out.println(moped.getVehicleInfo());
        System.out.println();



        List<Client> clientList = new ArrayList<>();
        ClientManager clientManager = new ClientManager(clientList);

        clientManager.registerClient("John", "Doe", "555-1234");
        clientManager.registerClient("Jane", "Smith", "555-5678");
        clientManager.registerClient("Alice", "Johnson", "555-8765");

        System.out.println("All Clients Info:");
        System.out.println(clientManager.getClientsInfo());

        //clientManager.unregisterClient("0987654321");

        //System.out.println("All Clients Info After Unregistering Jane Smith:");
       // System.out.println(clientManager.getClientsInfo());

    }
}