package app.entities;

public class OrderLine {

    private int order_line_id;
    private Base base;
    private Topping topping;
    private int quantity;
    private double price;
    private int customer_id;

    public OrderLine(Base base, Topping topping, int quantity, double price, int customer_id) {
        this.base = base;
        this.topping = topping;
        this.quantity = quantity;
        this.price = price;
        this.customer_id = customer_id;
    }

    public OrderLine(int order_line_id, Base base, Topping topping, int quantity, double price, int customer_id) {
        this.order_line_id = order_line_id;
        this.base = base;
        this.topping = topping;
        this.quantity = quantity;
        this.price = price;
        this.customer_id = customer_id;
    }

    public int getOrder_line_id() {
        return order_line_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public Base getBase() {
        return base;
    }

    public Topping getTopping() {
        return topping;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOrder_line_id(int order_line_id) {
        this.order_line_id = order_line_id;
    }
}