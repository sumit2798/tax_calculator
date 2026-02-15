package io.helidon.examples.tax;

import java.util.List;

public class CrmTaxResponse {
    private List<TaxResult> taxResults;
    private double totalTax;

    public CrmTaxResponse() {
    }

    public CrmTaxResponse(List<TaxResult> taxResults, double totalTax) {
        this.taxResults = taxResults;
        this.totalTax = totalTax;
    }

    public List<TaxResult> getTaxResults() {
        return taxResults;
    }

    public void setTaxResults(List<TaxResult> taxResults) {
        this.taxResults = taxResults;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }
}
