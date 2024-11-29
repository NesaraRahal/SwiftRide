package com.example.swiftride;

public class Notification {
    private int notificationId;
    private int senderId;
    private int receiverId;
    private int seat1;
    private int seat2;
    private int busId;
    private String status;
    private String time;

    // Constructor
    public Notification(int notificationId, int senderId, int receiverId, int seat1, int seat2, int busId, String status, String time) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.seat1 = seat1;
        this.seat2 = seat2;
        this.busId = busId;
        this.status = status;
        this.time = time;
    }

    // Getters
    public int getNotificationId() { return notificationId; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public int getSeat1() { return seat1; }
    public int getSeat2() { return seat2; }
    public int getBusId() { return busId; }
    public String getStatus() { return status; }
    public String getTime() { return time; }
}