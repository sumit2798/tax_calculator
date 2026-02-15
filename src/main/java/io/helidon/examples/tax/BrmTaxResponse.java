package io.helidon.examples.tax;

import jakarta.json.bind.annotation.JsonbProperty;

public class BrmTaxResponse {

    @JsonbProperty("CalculatedTax")
    private String calculatedTax;

    @JsonbProperty("TaxPercentage")
    private String taxPercentage;

    @JsonbProperty("GrossTaxamount")
    private String grossTaxAmount;

    @JsonbProperty("TaxCode")
    private String taxCode;

    @JsonbProperty("TransactionID")
    private String transactionId;

    public BrmTaxResponse() {
    }

    public BrmTaxResponse(String calculatedTax, String taxPercentage, String grossTaxAmount, String taxCode,
            String transactionId) {
        this.calculatedTax = calculatedTax;
        this.taxPercentage = taxPercentage;
        this.grossTaxAmount = grossTaxAmount;
        this.taxCode = taxCode;
        this.transactionId = transactionId;
    }

    public String getCalculatedTax() {
        return calculatedTax;
    }

    public void setCalculatedTax(String calculatedTax) {
        this.calculatedTax = calculatedTax;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getGrossTaxAmount() {
        return grossTaxAmount;
    }

    public void setGrossTaxAmount(String grossTaxAmount) {
        this.grossTaxAmount = grossTaxAmount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
