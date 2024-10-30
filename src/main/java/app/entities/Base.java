package app.entities;

public class Base{

    private int baseId;
    private String baseFlavor;
    private double price;

    public Base(int baseId, String baseFlavor, double price) {
        this.baseId = baseId;
        this.baseFlavor = baseFlavor;
        this.price = price;
    }

    public int getBaseId() {
        return baseId;
    }

    public String getBaseFlavor() {
        return baseFlavor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public void setBaseFlavor(String baseFlavor) {
        this.baseFlavor = baseFlavor;
    }
}