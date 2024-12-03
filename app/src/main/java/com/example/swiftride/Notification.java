package com.example.swiftride;

public class Notification {
    private int notificationId;
    private int senderId;
    private int receiverId;
    private int seat1;
    private int seat2;
    private String status;
    private String time;
    private int busId;



    public Notification(int notificationId, int senderId, int receiverId, int seat1, int seat2, String status, String time, int busId) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.seat1 = seat1;
        this.seat2 = seat2;
        this.status = status;
        this.time = time;
        this.busId = busId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    // Getters and setters

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSeat1() {
        return seat1;
    }

    public void setSeat1(int seat1) {
        this.seat1 = seat1;
    }

    public int getSeat2() {
        return seat2;
    }

    public void setSeat2(int seat2) {
        this.seat2 = seat2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }
}
