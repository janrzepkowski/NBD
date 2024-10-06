package org.nbd;

import org.nbd.models.*;

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
    }
}