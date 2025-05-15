package model;

public class Staff{
    private int staffId;
    private String name;
    private String email;
    private String position;
    private String address;
    private String status; // "Active" or "Inactive"

public Staff(int staffId, String name, String email, String position, String address, String status) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.position = position;
        this.address = address;
        this.status = status;
    }

    // Getters and Setters
    public int getStaffId() { 
        return staffId; }
    public void setStaffId(int staffId) { 
        this.staffId = staffId; }

    public String getName() { 
        return name; }
    public void setName(String name) { 
        this.name = name; }

    public String getEmail() { 
        return email; }
    public void setEmail(String email) { 
        this.email = email; }

    public String getPosition() { 
        return position; }
    public void setPosition(String position) { 
        this.position = position; }

    public String getAddress() { 
        return address; }
    public void setAddress(String address) { 
        this.address = address; }

    public String getStatus() { 
        return status; }
    public void setStatus(String status) { 
        this.status = status; }

}
