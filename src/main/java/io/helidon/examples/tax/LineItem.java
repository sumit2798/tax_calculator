package io.helidon.examples.tax;

public class LineItem {
    private String lineItemId;
    private String description;
    private double amount;

    public LineItem() {
    }

    public LineItem(String lineItemId, String description, double amount) {
        this.lineItemId = lineItemId;
        this.description = description;
        this.amount = amount;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Keeping getPrice/setPrice for backward compatibility if needed, but mapping
    // to amount
    public double getPrice() {
        return amount;
    }

    public void setPrice(double price) {
        this.amount = price;
    }
}
