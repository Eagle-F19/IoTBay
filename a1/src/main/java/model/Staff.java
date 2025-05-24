package model;

public class Staff {
    private int staffID;
    private String name;
    private String email;
    private String position;
    private String address;
    private String status;

    // Constructor with status
    public Staff(String name, String email, String position, String address) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.address = address;
        this.status = "active";
    }

    // Constructor with ID and status
    public Staff(int staffID, String name, String email, String position, String address, String status) {
        this.staffID = staffID;
        this.name = name;
        this.email = email;
        this.position = position;
        this.address = address;
        this.status = status;
    }

    // Getters
    public int getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPosition() {
        return position;
    }

    public String getAddress() {
        return address;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setters
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
} 