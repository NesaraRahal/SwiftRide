package com.example.swiftride;

public class Booking {

    private int reservationId;
    private String reservationDate;
    private int busId;
    private int seatNumber;
    private String startPoint;
    private String destinationPoint;

    // Constructor
    public Booking() {}

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    // Override toString to display in ListView
    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + "\n" +
                "Date: " + reservationDate + "\n" +
                "From: " + startPoint + " To: " + destinationPoint;
    }
}
