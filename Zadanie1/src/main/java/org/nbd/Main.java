package org.nbd;

import org.nbd.models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        // Test Rent class
        Client client = new Client("John", "Doe", "1234567890");
        String str1 = "2024-04-08 12:30";
        String str2 = "2024-04-15 14:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        Rent rent = new Rent(client, car, dateTime, dateTime2);
        System.out.println("Rent Info:");
        System.out.println(rent.getRentInfo());
        System.out.println();
    }
}