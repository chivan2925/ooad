package dto;

public class UsersDTO {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String userName;
    private String password;   
    private String role;


    public UsersDTO(String name, String phone, String address, String userName, String password) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.userName = userName;
        this.password = password;
    }

    public UsersDTO(String id, String name, String phone, String address, String userName, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.userName = userName;
        this.password = password;
    }

    public UsersDTO() {
        
    }

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    
}
