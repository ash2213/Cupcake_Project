package app.entities;

public class Customer {

    private int customer_id;
    private String email;
    private String password;
    private int balance;
    private boolean isAdmin;

    public Customer(int customer_id, String email, String password, int balance, boolean isAdmin) {
        this.customer_id = customer_id;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getEmail() {
        return email;
    }

    public int getBalance() {
        return balance;
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

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}