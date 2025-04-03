package model;

public class User {
    private String email;
    private String password;
    private String role;
    private double balance;

    public User(String email, String password, String role, double balance) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
