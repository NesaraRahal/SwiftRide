package com.example.swiftride;
public class Bus {
    private int id;
    private String licensePlate;
    private String routeNo;
    private String startRoute;
    private String destinationRoute;
    private int noSeats;
    private int ownerId;
    private int driverId;

    public Bus(int id, String licensePlate, String routeNo, String startRoute, String destinationRoute, int noSeats, int ownerId, int driverId) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.routeNo = routeNo;
        this.startRoute = startRoute;
        this.destinationRoute = destinationRoute;
        this.noSeats = noSeats;
        this.ownerId = ownerId;
        this.driverId = driverId;
    }

    // Add getters and setters here
    public String getLicensePlate() {
        return licensePlate;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public String getStartRoute() {
        return startRoute;
    }

    public String getDestinationRoute() {
        return destinationRoute;
    }

    public int getNoSeats() {
        return noSeats;
    }
}
