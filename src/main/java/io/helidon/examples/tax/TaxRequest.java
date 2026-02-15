package io.helidon.examples.tax;

import java.util.List;
import jakarta.json.bind.annotation.JsonbProperty;

public class TaxRequest {
    private String source;

    // CRM Fields
    private String accountType;
    private AccountAddress accountAddress;
    private List<LineItem> lineItems;

    // BRM Fields
    @JsonbProperty("Currency")
    private String currency;

    @JsonbProperty("TaxDate")
    private String taxDate;

    @JsonbProperty("TaxCode")
    private String taxCode;

    @JsonbProperty("AmountTaxed")
    private String amountTaxed;

    @JsonbProperty("TransactionID")
    private String transactionId;

    public TaxRequest() {
    }

    public TaxRequest(String source, List<LineItem> lineItems) {
        this.source = source;
        this.lineItems = lineItems;
    }

    @JsonbProperty("Source")
    public String getSource() {
        return source;
    }

    @JsonbProperty("Source")
    public void setSource(String source) {
        this.source = source;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public AccountAddress getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(AccountAddress accountAddress) {
        this.accountAddress = accountAddress;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(String taxDate) {
        this.taxDate = taxDate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAmountTaxed() {
        return amountTaxed;
    }

    public void setAmountTaxed(String amountTaxed) {
        this.amountTaxed = amountTaxed;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
