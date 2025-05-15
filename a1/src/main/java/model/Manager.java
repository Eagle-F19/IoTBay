public class Manager {
    private String name;
    private int mobile;
    private String email;
    private String password;

    public Manager(int staffId,String name, int mobile, String email, String password, String position, String address, String status) {
        this.staffId = staffId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.position = position;
        this.address = address;
        this.status = status;
    }


    // Standard getters and setters
    public int getStaffId() { 
        return staffId; 
    }
    
    public void setStaffId(int staffId) { 
        this.staffId = staffId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMobile() {
        return mobile;
    }
   public void setMobile(int mobile) {
        this.mobile = mobile;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() { 
        return position;
    }
    
    public void setPosition(String position) { 
        this.position = position;
    }

    public String getAddress() { 
        return address;
    }
    
    public void setAddress(String address) { 
        this.address = address;
    }

    public String getStatus() { 
        return status;
    }
    
    public void setStatus(String status) { 
        this.status = status;
    }
}
