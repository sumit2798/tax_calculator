package io.helidon.examples.tax;

public class TaxResponse {
    private double totalPrice;
    private double totalTax;
    private double grandTotal;

    public TaxResponse() {
    }

    public TaxResponse(double totalPrice, double totalTax, double grandTotal) {
        this.totalPrice = totalPrice;
        this.totalTax = totalTax;
        this.grandTotal = grandTotal;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }
}
