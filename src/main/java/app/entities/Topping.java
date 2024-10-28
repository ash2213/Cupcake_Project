package app.entities;

public class Topping {

    private int toppingId;
    private String toppingFlavor;
    private double price;

    public Topping(int toppingId, String toppingFlavor, double price) {
        this.toppingId = toppingId;
        this.toppingFlavor = toppingFlavor;
        this.price = price;
    }

    public int getToppingId() {
        return toppingId;
    }

    public String getToppingFlavor() {
        return toppingFlavor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public void setToppingFlavor(String toppingFlavor) {
        this.toppingFlavor = toppingFlavor;
    }
}