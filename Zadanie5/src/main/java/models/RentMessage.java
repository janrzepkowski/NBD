package models;

public class RentMessage {
    private Rent rent;
    private String rentalName;

    public RentMessage() {
    }

    public RentMessage(Rent rent, String rentalName) {
        this.rent = rent;
        this.rentalName = rentalName;
    }

    public Rent getRent() {
        return rent;
    }

    public void setRent(Rent rent) {
        this.rent = rent;
    }

    public String getRentalName() {
        return rentalName;
    }

    public void setRentalName(String rentalName) {
        this.rentalName = rentalName;
    }
}