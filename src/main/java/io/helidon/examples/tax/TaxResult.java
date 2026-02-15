package io.helidon.examples.tax;

public class TaxResult {
    private String lineItemId;
    private double taxAmount;
    private String taxRate;

    public TaxResult() {
    }

    public TaxResult(String lineItemId, double taxAmount, String taxRate) {
        this.lineItemId = lineItemId;
        this.taxAmount = taxAmount;
        this.taxRate = taxRate;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
}
