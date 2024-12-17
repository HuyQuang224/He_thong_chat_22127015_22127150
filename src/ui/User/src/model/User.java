package model;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String address;
    private String dob;
    private String gender;
    private String email;
    private String password;
    private String status;
    private String role;
    private boolean banned;

    public User() {}

    public User(int id, String username){
        this.id = id;
        this.username = username;
    }

    public User(int id, String username, String status){
        this.id = id;
        this.username = username;
        this.status = status;
    }

    public User(String username, int id, String role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public User(String username, String fullname, int id){
        this.id = id;
        this.username = username;
        this.fullName = fullname;
    }

    public User(int id, String username, String fullname, String status){
        this.id = id;
        this.username = username;
        this.fullName = fullname;
        this.status = status;
    }

    public User(int id, String username, String fullName, String address, String dob, String gender, String email, String password, boolean banned, String status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.banned = banned;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDob() {
        return this.dob;
    }

    public String getGender() {
        return this.gender;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getStatus(){
        return this.status;
    }

    public String getRole() {
        return this.role;
    }

    public boolean getBanned() {
        return this.banned;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
