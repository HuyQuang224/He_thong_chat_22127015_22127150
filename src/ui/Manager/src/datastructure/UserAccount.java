package datastructure;

import java.util.Date;

public class UserAccount {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String address;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private boolean online;
    private Date createdAt;
    private boolean banned;

    // Constructor nhận mật khẩu mới
    public UserAccount(String password) {
        this.password = password;
    }

    public UserAccount(int id, String username) {
        this.username = username;
        this.id = id;
    }
    
    public UserAccount() {
        this.id = -1;
        this.username = "";
        this.password = "";
        this.fullName = "";
        this.address = "";
        this.dateOfBirth = null;
        this.gender = "";
        this.email = "";
        this.online = true;
        this.createdAt = null;
        this.banned = true;
    }

    public UserAccount(boolean isBanned) {
        this.banned = isBanned;
    }

    public UserAccount(int id, String username, String password, String fullName, String address, Date dateOfBirth, String gender, String email, boolean online, Date createdAt, boolean banned) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.online = online;
        this.createdAt = createdAt;
        this.banned = banned;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) { this.online = online; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public boolean isBanned() { return banned; }
    public void setBanned(boolean banned) { this.banned = banned; }
}
