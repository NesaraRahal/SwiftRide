package com.example.swiftride;

public class ReservationInfo {
    private int passengerCount;
    private String startPoint;
    private String destinationPoint;
    private int busId;

    private String reservationDate;

    public ReservationInfo(int passengerCount, String startPoint, String destinationPoint, int busId, String date) {
        this.passengerCount = passengerCount;
        this.startPoint = startPoint;
        this.destinationPoint = destinationPoint;
        this.busId = busId;
        this.reservationDate = date;
    }

    // Getters and setters
    public int getPassengerCount() { return passengerCount; }
    public String getStartPoint() { return startPoint; }
    public String getDestinationPoint() { return destinationPoint; }
    public int getBusId() { return busId; }

    public String getReservationDate() { return reservationDate; }
}
