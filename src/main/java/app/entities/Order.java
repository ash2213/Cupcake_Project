package app.entities;
import java.sql.Timestamp;
import java.util.List;

public class Order{
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

    public int getCustomerId() {
        return customerId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
}