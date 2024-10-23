package app.entities;

public class OrderLine {

    private Base base;
    private Topping topping;
    private int quantity;
    private double price;

    public OrderLine(Base base, Topping topping, int quantity, double price) {
        this.base = base;
        this.topping = topping;
        this.quantity = quantity;
        this.price = price;
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
}
