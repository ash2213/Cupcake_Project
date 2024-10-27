package app.entities;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private int customerId;
    private double totalPrice;
    private String status;
    private Timestamp orderDate;
    private List<OrderLine> orderLines;


    public Order(int customerId, double totalPrice, String status, Timestamp orderDate) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Order(int orderId, int customerId, double totalPrice, String status, Timestamp orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
}