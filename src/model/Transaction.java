package model;

public class Transaction {
    private int id;
    private String userEmail;
    private double amount;
    private String type;
    private String timestamp;

    public Transaction(int id, String userEmail, double amount, String type, String timestamp) {
        this.id = id;
        this.userEmail = userEmail;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getTimestamp() { return timestamp; }
}
